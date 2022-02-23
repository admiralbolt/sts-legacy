package legacy.cards.mods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

/**
 * Spells! Scale with focus instead of Strength or Dexterity! Most of the logic is in the Spell base class.
 * This is mostly just for visual clarity.
 */
public class SpellModifier extends ModifierWithBadge {

  public static final String ID = "legacy:spell";

  public SpellModifier() {
    super(ID);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new SpellModifier();
  }

  @Override
  public String identifier(AbstractCard card) {
    return ID;
  }
}
