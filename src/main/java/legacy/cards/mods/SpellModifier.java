package legacy.cards.mods;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Texture;
import legacy.util.TextureLoader;

/**
 *
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
}
