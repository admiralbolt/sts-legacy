package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class EquipmentTrait extends AbstractCardModifier {

  // The trait keyword should always match the id.
  public final String id;

  public EquipmentTrait(String id) {
    this.id = id;
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
