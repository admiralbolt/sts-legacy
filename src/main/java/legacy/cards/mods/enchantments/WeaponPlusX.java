package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.LegacyCard;

/**
 * Enchantment that gives a weapon +X damage.
 */
public class WeaponPlusX extends Enchantment {

  public static final String ID = "legacy:weapon_plus_x";
  public int amount;

  public WeaponPlusX(int amount) {
    super(ID, "+" + amount, "", LegacyCard.LegacyCardType.WEAPON, AbstractCard.CardRarity.SPECIAL, 0, 0);

    this.amount = amount;
  }

  @Override
  public float modifyDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
    return damage + this.amount;
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new WeaponPlusX(this.amount);
  }

}
