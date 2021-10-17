package legacy.cards.weapons;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import legacy.LegacyMod;
import legacy.characters.TheDefault;
import legacy.db.DBCardInfo;
import legacy.enchantments.Enchantment;
import legacy.powers.FlurryPower;
import legacy.util.CardUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Default base case to abstract some things...
 */
public class LegacyWeapon extends CustomCard {

  public enum WeaponTrait {
    FINESSE,
    PAIRED,
    RANGED
  }

  private final Set<WeaponTrait> traits;
  private final List<Enchantment> enchantments;
  private final CardStrings cardStrings;

  public LegacyWeapon(String id, CardStrings cardStrings, int cost, CardRarity rarity, CardTarget target) {
    this(id, cardStrings, cost, rarity, target, new WeaponTrait[]{});
  }

  public LegacyWeapon(String id, CardStrings cardStrings, int cost, CardRarity rarity, CardTarget target, WeaponTrait... weaponTraits) {
    super(
            id,
            LegacyMod.LEGACY_DB.getName(id, cardStrings.NAME),
            LegacyMod.makeCardPathFromId(id),
            cost,
            LegacyMod.LEGACY_DB.getCardDescription(id, cardStrings.DESCRIPTION),
            CardType.ATTACK,
            TheDefault.Enums.COLOR_GRAY,
            rarity,
            target
    );


    DBCardInfo info = LegacyMod.LEGACY_DB.getCardInfo(id);
    if (info.numUpgrades > 0) {
      this.timesUpgraded = info.numUpgrades;
      this.upgraded = true;
      this.initializeTitle();
    }
    this.baseDamage = info.value;

    this.magicNumber = 1;
    this.baseMagicNumber = 1;
    this.cardStrings = cardStrings;
    this.enchantments = LegacyMod.LEGACY_DB.loadCardEnchantments(id);

    this.traits = new HashSet<>();
    this.traits.addAll(Arrays.asList(weaponTraits));
  }

    /**
     * Returns a nicely formatted name based on upgrades & enchantments.
     *
     * Ex: +2 Corrosive Returning Greatsword
     */
  public String getFormattedName() {
    StringBuilder builder = new StringBuilder();
    if (this.timesUpgraded > 0) {
      builder.append("+");
      builder.append(this.timesUpgraded);
      builder.append(" ");
    }

    for (Enchantment enchantment : this.enchantments) {
      builder.append(enchantment.name);
      builder.append(" ");
    }

    builder.append(this.cardStrings.NAME);
    return builder.toString();
  }

  public void upgradeStats() {
    this.upgradeDamage(1);
    this.upgraded = true;
    ++this.timesUpgraded;
    this.name = this.getFormattedName();
    this.initializeTitle();
  }

  /**
   * When a legacy weapon gets upgraded, we want to permanently increase the damage by 1. This means:
   * 1. Updating the damage & name for all instances of the current card within the current run.
   * 2. Updating the database so that upgrades are properly loaded on subsequent runs.
   *
   * The way that upgrading actually works though, is that a duplicate card gets created with upgrade()
   * called on that. We need to ensure that when upgrade() is called on a card *actually* in our deck
   * that the database gets updated, but ONLY in that case AND we don't want to loop infinitely.
   */
  @Override
  public void upgrade() {
    this.upgradeStats();

    // This is a terrible hack -- when we upgrade a card we check to make sure it's actually in our deck.
    if (!CardUtils.isCardInstanceInDeck(this.uuid)) return;

    // Also, if we have temporary upgrade effects they should also only apply to a single instance of the card.
    // This will mean that Lesson Learned won't work until this is handled properly.
    if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom) return;

    // If we get here, we need to update ALL other instances of this card in our deck,
    // and persist changes to the DB.
    for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
      if (card.uuid.equals(this.uuid)) continue;
      if (!card.cardID.equals(this.cardID)) continue;
      if (!(card instanceof LegacyWeapon)) continue;

      ((LegacyWeapon) card).upgradeStats();
    }

    LegacyMod.LEGACY_DB.upgradeCard(this.cardID);
  }

  @Override
  public boolean canUpgrade() {
    return true;
  }

  /**
   * Applies effects from all enchantments.
   */
  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (this.traits.contains(WeaponTrait.PAIRED)) {
      AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FlurryPower(p, 1)));
    }
    for (Enchantment enchantment : this.enchantments) {
      enchantment.apply(p, m);
    }
  }

  @Override
  public void applyPowers() {
    int strengthAmount = 0;
    AbstractPower strength = AbstractDungeon.player.getPower("Strength");

    if (this.traits.contains(WeaponTrait.FINESSE) || this.traits.contains(WeaponTrait.RANGED)) {
      if (strength != null) {
        strengthAmount = strength.amount;
        strength.amount = 0;
      }
    }

    super.applyPowers();

    if (strength != null && strengthAmount != 0) {
      strength.amount = strengthAmount;
    }

    if (this.traits.contains(WeaponTrait.FINESSE)) {
      AbstractPower dexterity = AbstractDungeon.player.getPower("Dexterity");
      if (dexterity != null && dexterity.amount != 0) {
        if (this.isMultiDamage && this.multiDamage != null) {
          for (int i = 0; i < this.multiDamage.length; ++i) {
            this.multiDamage[i] += dexterity.amount;
          }
        }
        this.damage += dexterity.amount;
        this.isDamageModified = true;
      }
    }

    // Paired weapons deal bonus damage based on the amount of flurry stacks.
    if (this.traits.contains(WeaponTrait.PAIRED)) {
      AbstractPower flurry = AbstractDungeon.player.getPower("legacy:flurry");
      if (flurry != null && flurry.amount != 0) {
        if (this.isMultiDamage && this.multiDamage != null) {
          for (int i = 0; i < this.multiDamage.length; ++i) {
            this.multiDamage[i] += flurry.amount;
          }
        }
        this.damage += flurry.amount;
        this.isDamageModified = true;
      }
    }
  }

  @Override
  public void calculateCardDamage(AbstractMonster mo) {
    int strengthAmount = 0;
    AbstractPower strength = AbstractDungeon.player.getPower("Strength");

    if (this.traits.contains(WeaponTrait.FINESSE)) {
      if (strength != null) {
        strengthAmount = strength.amount;
        strength.amount = 0;
      }
    }

    super.calculateCardDamage(mo);

    if (this.traits.contains(WeaponTrait.FINESSE)) {
      if (strength != null && strengthAmount != 0) {
        strength.amount = strengthAmount;
      }

      // Then, we apply damage from Dexterity.
      AbstractPower dexterity = AbstractDungeon.player.getPower("Dexterity");
      if (dexterity != null && dexterity.amount != 0) {
        if (this.isMultiDamage && this.multiDamage != null) {
          for (int i = 0; i < this.multiDamage.length; ++i) {
            this.multiDamage[i] += dexterity.amount;
          }
        }
        this.damage += dexterity.amount;
        this.isDamageModified = true;
      }
    }

    // Paired weapons deal bonus damage based on the amount of flurry stacks.
    if (this.traits.contains(WeaponTrait.PAIRED)) {
      AbstractPower flurry = AbstractDungeon.player.getPower("legacy:flurry");
      if (flurry != null && flurry.amount != 0) {
        if (this.isMultiDamage && this.multiDamage != null) {
          for (int i = 0; i < this.multiDamage.length; ++i) {
            this.multiDamage[i] += flurry.amount;
          }
        }
        this.damage += flurry.amount;
        this.isDamageModified = true;
      }
    }
  }
}
