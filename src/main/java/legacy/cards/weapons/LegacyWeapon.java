package legacy.cards.weapons;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import legacy.cards.LegacyCard;
import legacy.enchantments.Enchantment;
import legacy.powers.FlurryPower;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Weapons of Legacy.
 *
 * These are attacks that can be **permanently** upgraded.
 */
public class LegacyWeapon extends LegacyCard {

  public enum WeaponTrait {
    FINESSE,
    PAIRED,
    RANGED,
    TWO_HANDED
  }

  private final Set<WeaponTrait> traits;

  // A temporary variable used to hold current strength. This is only used for damage calculation to make strength
  // apply twice as much for two handed weapons.
  private int currentStrength = 0;

  public LegacyWeapon(String id, CardStrings cardStrings, int cost, CardRarity rarity, CardTarget target) {
    this(id, cardStrings, cost, rarity, target, new WeaponTrait[]{});
  }

  public LegacyWeapon(String id, CardStrings cardStrings, int cost, CardRarity rarity, CardTarget target, WeaponTrait... weaponTraits) {
    super(id, cardStrings, cost, CardType.ATTACK, rarity, target);

    this.traits = new HashSet<>();
    this.traits.addAll(Arrays.asList(weaponTraits));
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
    if (this.traits.contains(WeaponTrait.TWO_HANDED)) this.addDamage(strengthAmount);

    // Finese weapons scale with dexterity instead of strength.
    // In the case where something is two handed and finesse, it should scale 1x with strength and dexterity.
    if (this.traits.contains(WeaponTrait.FINESSE)) {
      this.addDamage(dexterityAmount);
      this.addDamage(-1 * strengthAmount);
    }

    // Paired weapons scale with flurry stacks.
    if (this.traits.contains(WeaponTrait.PAIRED)) {
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
