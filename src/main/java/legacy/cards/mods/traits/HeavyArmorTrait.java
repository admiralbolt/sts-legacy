package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Texture;
import legacy.cards.mods.ModifierWithBadge;
import legacy.util.TextureLoader;

/**
 * Heavy armor does not scale with dexterity.
 */
public class HeavyArmorTrait extends ModifierWithBadge {

  public static final String ID = "legacy:heavy_armor";
  public static final Texture BADGE = TextureLoader.getTexture("legacy/images/cards/mods/traits/heavy_armor.png");

  public HeavyArmorTrait() {
    super(ID);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new HeavyArmorTrait();
  }

}
