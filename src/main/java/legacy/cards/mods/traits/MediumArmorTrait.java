package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Texture;
import legacy.cards.mods.ModifierWithBadge;
import legacy.util.TextureLoader;

/**
 * Medium armor scales with dex up to a cap of 3.
 */
public class MediumArmorTrait extends ModifierWithBadge {

  public static final String ID = "legacy:medium_armor";
  public static final Texture BADGE = TextureLoader.getTexture("legacy/images/cards/mods/traits/medium_armor.png");

  public MediumArmorTrait() {
    super(ID);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new MediumArmorTrait();
  }

}
