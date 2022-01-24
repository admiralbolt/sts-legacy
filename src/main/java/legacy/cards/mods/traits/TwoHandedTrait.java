package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import legacy.util.TextureLoader;

public class TwoHandedTrait extends EquipmentTrait {

  public static final String ID = "legacy:two_handed";
  public static final Texture BADGE = TextureLoader.getTexture("legacy/images/cards/mods/traits/two_handed.png");

  public TwoHandedTrait() {
    super("two_handed");
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new TwoHandedTrait();
  }

  @Override
  public String identifier(AbstractCard card) {
    return ID;
  }

}
