package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;

public class EquipmentTrait extends AbstractCardModifier {

  public final String keyword;

  public EquipmentTrait(String baseName) {
    this.keyword = baseName;
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return null;
  }

}
