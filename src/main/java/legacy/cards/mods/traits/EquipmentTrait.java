package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class EquipmentTrait extends AbstractCardModifier {

  // The trait keyword should always match the id.
  public final String id;

  // We track badge as both a static reference on the trait and as a class member. Both have their uses.
  public final Texture badge;

  public EquipmentTrait(String id, Texture badge) {
    this.id = id;
    this.badge = badge;
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return null;
  }

  @Override
  public String identifier(AbstractCard card) {
    return this.id;
  }
}
