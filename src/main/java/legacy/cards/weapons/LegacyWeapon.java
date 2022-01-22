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

  @Override
  public void applyPowers() {
    AbstractPower strength = AbstractDungeon.player.getPower("Strength");
    int strengthAmount = (strength == null) ? 0 : strength.amount;

    if (strength != null) {
      // Two handed weapons scale with strength twice as much.
      if (this.traits.contains(WeaponTrait.TWO_HANDED)) strength.amount *= 2;
        // Finesse and ranged weapons don't scale with strength UNLESS
        // they are also marked as two handed weapons.
      else if (this.traits.contains(WeaponTrait.FINESSE) || this.traits.contains(WeaponTrait.RANGED))
        strength.amount -= strengthAmount;
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
