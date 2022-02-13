package legacy.cards.vars;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import legacy.cards.LegacyCard;

/**
 * An extra dynamic variable for showing the strength of spells.
 *
 * This is used for spells that need multiple magic numbers. I.e. Augury uses it's normal magic number for its scry
 * amount, but needs a second magic number for the draw amount.
 */
public class MagicNumberTwoVariable extends DynamicVariable {

  // Use !M2! in descriptions for this variable.
  @Override
  public String key() {
    return "M2";
  }

  @Override
  public boolean isModified(AbstractCard card) {
    if (!(card instanceof LegacyCard)) return false;

    return ((LegacyCard) card).isMagicNumberTwoModified;
  }

  @Override
  public int value(AbstractCard card) {
    if (!(card instanceof LegacyCard)) return 0;

    return ((LegacyCard) card).magicNumberTwo;
  }

  @Override
  public int baseValue(AbstractCard card) {
    if (!(card instanceof LegacyCard)) return 0;

    return ((LegacyCard) card).baseMagicNumberTwo;
  }

  @Override
  public boolean upgraded(AbstractCard card) {
    if (!(card instanceof LegacyCard)) return false;

    return ((LegacyCard) card).upgradedMagicNumberTwo;
  }
}
