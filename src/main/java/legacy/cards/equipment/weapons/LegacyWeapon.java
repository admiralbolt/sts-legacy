package legacy.cards.equipment.weapons;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import legacy.actions.PiercingDamageAction;
import legacy.cards.LegacyCard;
import legacy.cards.mods.enchantments.Cleaving;
import legacy.cards.mods.enchantments.EnchantmentUtils;
import legacy.cards.mods.traits.FinesseTrait;
import legacy.cards.mods.traits.FlurryTrait;
import legacy.cards.mods.traits.RangedTrait;
import legacy.cards.mods.traits.TwoHandedTrait;
import legacy.powers.FlurryPower;

import java.util.ArrayList;

/**
 * Weapons of Legacy.
 *
 * These are attacks that can be **permanently** upgraded via enchantments.
 * Additionally, each weapon has certain traits associated with it that affect the card.
 * For example, Finesse weapons have their damage scale with Dexterity instead of Strength.
 *
 * One last thing, because of the weirdness from loading values from the DB, the base values for these cards
 * are actually defined in LegacyCards.java.
 */
public abstract class LegacyWeapon extends LegacyCard implements SpawnModificationCard {

  public LegacyWeapon(String id, int cost, CardRarity rarity, CardTarget target, AbstractCardModifier ...modifiers) {
    super(id, cost, LegacyCardType.WEAPON, CardType.ATTACK, rarity, target);

    this.enchantable = true;
    this.baseMagicNumber = this.magicNumber = 1;

    // All enchantments for this card should be loaded.
    for (AbstractCardModifier modifier : EnchantmentUtils.PERSISTED_ENCHANTMENTS.getOrDefault(this.cardID, new ArrayList<>())) {
      CardModifierManager.addModifier(this, modifier);
      // Cleaving should target all instead of just one.
      if (modifier.identifier(this).equals(Cleaving.ID)) {
        this.target = CardTarget.ALL_ENEMY;
      }
    }

    // All the weapon traits should be applied.
    for (AbstractCardModifier modifier : modifiers) {
      CardModifierManager.addModifier(this, modifier);
    }

    this.updateName();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    // NOTE: Enchantment effects are automatically added via their onUse() hook, with the exception of Cleaving
    // which needs our damage to hit all enemies.
    if (CardModifierManager.hasModifier(this, RangedTrait.ID)) {
      AbstractDungeon.actionManager.addToBottom(new PiercingDamageAction(m, p, damage));
    } else {
      if (this.isMultiDamage) {
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
      } else {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
      }
    }

    if (CardModifierManager.hasModifier(this, FlurryTrait.ID)) {
      AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FlurryPower(p, 1)));
    }
  }

  // The logic of applyPower() / calculateCardDamage() is almost identical for handling weapon traits.
  // Rather than repeating ourselves we do our damage calculation in a shared method. This works slightly differently
  // than how the base game handles some things.
  //
  // HeavyBlade for example, multiplies the strength value before calling super.applyPowers() and then resets it
  // afterwords. That seems a little fragile to me, so instead this.damage is modified directly instead.
  private void damageCalc() {
    AbstractPower strength = AbstractDungeon.player.getPower("Strength");
    AbstractPower dexterity = AbstractDungeon.player.getPower("Dexterity");

    int strengthAmount = (strength == null) ? 0 : strength.amount;
    int dexterityAmount = (dexterity == null) ? 0 : dexterity.amount;

    // Two handed weapons scale with strength twice as much.
    if (CardModifierManager.hasModifier(this, TwoHandedTrait.ID)) this.addDamage(strengthAmount);

    // Finesse weapons scale with dexterity instead of strength.
    // In the case where something is two handed and finesse, it should scale 1x with strength and dexterity.
    if (CardModifierManager.hasModifier(this, FinesseTrait.ID)) {
      this.addDamage(dexterityAmount);
      this.addDamage(-1 * strengthAmount);
    }

    // Paired weapons scale with flurry stacks.
    if (CardModifierManager.hasModifier(this, FlurryTrait.ID)) {
      AbstractPower flurry = AbstractDungeon.player.getPower(FlurryPower.POWER_ID);
      if (flurry != null) addDamage(flurry.amount);
    }

    this.isDamageModified = (this.damage != this.baseDamage);
  }

  private void addDamage(int amount) {
    if (this.isMultiDamage && this.multiDamage != null) {
      for (int i = 0; i < this.multiDamage.length; ++i) {
        this.multiDamage[i] += amount;
      }
    }
    this.damage += amount;
  }

  @Override
  public void applyPowers() {
    super.applyPowers();
    this.damageCalc();
  }

  @Override
  public void calculateCardDamage(AbstractMonster mo) {
    super.calculateCardDamage(mo);
    this.damageCalc();
  }

  // Weapon cards are unique! If you already have a copy in your deck, you can't get more.
  @Override
  public boolean canSpawn(ArrayList<AbstractCard> currentRewardCards) {
    return AbstractDungeon.player.masterDeck.group.stream().noneMatch((card) -> card.cardID.equals(this.cardID));
  }
}
