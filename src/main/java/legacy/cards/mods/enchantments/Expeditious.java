package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import legacy.cards.LegacyCard;

/**
 * Enchantment that gives armor -1 cost and exhaust.
 */
public class Expeditious extends Enchantment {

  public static String ID = "legacy:expeditious";

  public Expeditious() {
    super(ID, "Expeditious", "Costs [E] less & exhausts.", LegacyCard.LegacyCardType.ARMOR, AbstractCard.CardRarity.UNCOMMON, 8, 1);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Expeditious();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#8656c5");
  }

  @Override
  public void onInitialApplication(AbstractCard card) {
    super.onInitialApplication(card);
    card.updateCost(-1);
    card.exhaust = true;
    card.initializeDescription();
  }

}
