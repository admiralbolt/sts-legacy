package legacy.cards.equipment.weapons;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import legacy.actions.PiercingDamageAction;
import legacy.cards.equipment.EquipmentCard;
import legacy.cards.mods.traits.FinesseTrait;
import legacy.cards.mods.traits.FlurryTrait;
import legacy.cards.mods.traits.RangedTrait;
import legacy.cards.mods.traits.TwoHandedTrait;
import legacy.powers.FlurryPower;

/**
 * Weapons of Legacy.
 *
 * These are attacks that can be **permanently** upgraded.
 */
public class LegacyWeapon extends EquipmentCard {

  public LegacyWeapon(String id, CardStrings cardStrings, int cost, CardRarity rarity, CardTarget target) {
    super(id, cardStrings, cost, CardType.ATTACK, rarity, target);
  }

  public LegacyWeapon(String id, CardStrings cardStrings, int cost, CardRarity rarity, CardTarget target, AbstractCardModifier ...modifiers) {
    super(id, cardStrings, cost, CardType.ATTACK, rarity, target);

    for (AbstractCardModifier modifier : modifiers) {
      CardModifierManager.addModifier(this, modifier);
    }
  }

  /**
   * Applies effects from all enchantments.
   */
  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
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

    // Finese weapons scale with dexterity instead of strength.
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
}
