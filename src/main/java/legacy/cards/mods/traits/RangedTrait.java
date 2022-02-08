package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Texture;
import legacy.cards.mods.ModifierWithBadge;
import legacy.util.TextureLoader;

/**
 * Ranged weapons ignore block, thorns, curl up, malleable, and sharp hide.
 *
 * Logic for damage calculation is done in PiercingDamageAction.
 */
public class RangedTrait extends ModifierWithBadge {

  public static final String ID = "legacy:ranged";
  public static final Texture BADGE = TextureLoader.getTexture("legacy/images/cards/mods/traits/ranged.png");

  public RangedTrait() {
    super(ID);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new RangedTrait();
  }

}
