package legacy.cards.equipment.weapons;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import legacy.LegacyMod;
import legacy.actions.PiercingDamageAction;
import legacy.cards.LegacyCard;
import legacy.cards.mods.enchantments.Enchantment;
import legacy.cards.mods.traits.FinesseTrait;
import legacy.cards.mods.traits.FlurryTrait;
import legacy.cards.mods.traits.RangedTrait;
import legacy.cards.mods.traits.TwoHandedTrait;
import legacy.db.DBCardInfo;
import legacy.powers.FlurryPower;

import java.util.ArrayList;
import java.util.List;

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

  public static String getImagePath(String id) {
    return LegacyMod.MOD_ID + "/images/cards/weapons/" + LegacyMod.getNameFromId(id) + ".png";
  }

  public List<Enchantment> enchantments;
  public final CardStrings cardStrings;

  public LegacyWeapon(String id, CardStrings cardStrings, int cost, CardRarity rarity, CardTarget target, AbstractCardModifier ...modifiers) {
    super(id, LegacyMod.LEGACY_DB.getName(id, cardStrings.NAME), getImagePath(id), cost,
            cardStrings.DESCRIPTION, CardType.ATTACK, rarity, target);

    this.cardStrings = cardStrings;

    // Properly load permanent upgrades / name from the db.
    DBCardInfo info = LegacyMod.LEGACY_DB.getCardInfo(id);
    this.baseDamage = info.value;

    this.baseMagicNumber = this.magicNumber = 1;
    // Enchantments need to get loaded from the DB, and their modifiers need to be applied.
    this.enchantments = LegacyMod.LEGACY_DB.loadCardEnchantments(id);
    for (Enchantment enchantment : this.enchantments) {
      CardModifierManager.addModifier(this, enchantment);
    }

    // All the weapon traits should be applied.
    for (AbstractCardModifier modifier : modifiers) {
      CardModifierManager.addModifier(this, modifier);
    }
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    // NOTE: Enchantment effects are automatically added via their onUse() hook, so no need to do anything here.
    if (CardModifierManager.hasModifier(this, FlurryTrait.ID)) {
      AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FlurryPower(p, 1)));
    }

    if (CardModifierManager.hasModifier(this, RangedTrait.ID)) {
      AbstractDungeon.actionManager.addToBottom(new PiercingDamageAction(m, p, damage));
    } else {
      AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
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
      AbstractPower flurry = AbstractDungeon.player.getPower("legacy:flurry");
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
    for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
      if (card.cardID.equals(this.cardID)) return false;
    }

    return true;
  }
}
