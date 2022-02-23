package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import legacy.cards.mods.ModifierWithBadge;

/**
 * Medium armor scales with dex up to a cap of 3.
 */
public class MediumArmorTrait extends ModifierWithBadge {

  public static final String ID = "legacy:medium_armor";

  public MediumArmorTrait() {
    super(ID);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new MediumArmorTrait();
  }

}
