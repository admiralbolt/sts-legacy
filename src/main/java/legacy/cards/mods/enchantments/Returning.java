package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import legacy.cards.LegacyCard;

/**
 * Enchantment that causes weapons to return to your deck instead of going to your discard.
 */
public class Returning extends Enchantment {

  public static final String ID = "legacy:returning";

  public Returning() {
    super(ID, "Returning", "Shuffle this card into your draw pile.", LegacyCard.LegacyCardType.WEAPON, AbstractCard.CardRarity.UNCOMMON, 4, 2);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Returning();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#f22e8a");
  }

  @Override
  public void onInitialApplication(AbstractCard card) {
    super.onInitialApplication(card);
    card.shuffleBackIntoDrawPile = true;
    card.initializeDescription();
  }

}
