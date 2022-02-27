package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import legacy.cards.LegacyCard;

/**
 * Enchantment that causes weapons to gain retain.
 */
public class Wise extends Enchantment {

  public static final String ID = "legacy:wise";

  public Wise() {
    super(ID, "Wise", "Retain.", LegacyCard.LegacyCardType.WEAPON, AbstractCard.CardRarity.UNCOMMON, 6, 2);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Wise();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#8433ff");
  }

  @Override
  public void onInitialApplication(AbstractCard card) {
    super.onInitialApplication(card);
    card.retain = true;
    card.initializeDescription();
  }

}
