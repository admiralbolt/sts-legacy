package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import legacy.cards.LegacyCards;

/**
 * Enchantment that gives an armor +X damage.
 */
public class ArmorPlusX extends Enchantment {

  public int amount;

  public ArmorPlusX(int amount) {
    super("legacy:armor_plus_x", "+" + amount, "", LegacyCards.EquipmentType.ARMOR, 0, 0);
  }

  @Override
  public float modifyBlock(float block, AbstractCard card) {
    return block + this.amount;
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new ArmorPlusX(this.amount);
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#aaaaaa");
  }

}
