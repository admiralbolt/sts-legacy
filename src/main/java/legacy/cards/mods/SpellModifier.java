package legacy.cards.mods;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import legacy.util.TextureLoader;

/**
 * Spells! Scale with focus instead of Strength or Dexterity! Most of the logic is in the Spell base class.
 * This is mostly just for visual clarity.
 */
public class SpellModifier extends ModifierWithBadge {

  public static final String ID = "legacy:spell";
  public static final Texture BADGE = TextureLoader.getTexture("legacy/images/cards/mods/spell.png");

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
