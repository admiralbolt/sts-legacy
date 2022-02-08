package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Texture;
import legacy.cards.mods.ModifierWithBadge;
import legacy.util.TextureLoader;

/**
 * Two handed weapons have +1x scaling with strength.
 *
 * Logic for damage calculation is in LegacyWeapon.
 */
public class TwoHandedTrait extends ModifierWithBadge {

  public static final String ID = "legacy:two_handed";
  public static final Texture BADGE = TextureLoader.getTexture("legacy/images/cards/mods/traits/two_handed.png");

  public TwoHandedTrait() {
    super(ID);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new TwoHandedTrait();
  }

}
