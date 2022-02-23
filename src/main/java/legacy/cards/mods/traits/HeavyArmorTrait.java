package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import legacy.cards.mods.ModifierWithBadge;

/**
 * Heavy armor does not scale with dexterity.
 */
public class HeavyArmorTrait extends ModifierWithBadge {

  public static final String ID = "legacy:heavy_armor";

  public HeavyArmorTrait() {
    super(ID);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new HeavyArmorTrait();
  }

}
