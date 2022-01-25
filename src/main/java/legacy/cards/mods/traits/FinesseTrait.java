package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Texture;
import legacy.util.TextureLoader;

/**
 * Two handed weapons have +1x scaling with strength.
 */
public class FinesseTrait extends EquipmentTrait {

  public static final String ID = "legacy:finesse";
  public static final Texture BADGE = TextureLoader.getTexture("legacy/images/cards/mods/traits/finesse.png");

  public FinesseTrait() {
    super(ID, BADGE);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new TwoHandedTrait();
  }

}
