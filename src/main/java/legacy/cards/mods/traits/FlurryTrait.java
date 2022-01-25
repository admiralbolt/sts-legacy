package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Texture;
import legacy.util.TextureLoader;

/**
 * Flurry weapons deal more damage the more you've played in a single  turn.
 *
 * Logic for damage calculation is in LegacyWeapon.
 */
public class FlurryTrait extends EquipmentTrait {

  public static final String ID = "legacy:flurry";
  public static final Texture BADGE = TextureLoader.getTexture("legacy/images/cards/mods/traits/flurry.png");

  public FlurryTrait() {
    super(ID, BADGE);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new FlurryTrait();
  }

}
