package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import legacy.cards.mods.ModifierWithBadge;

/**
 * Two handed weapons have +1x scaling with strength.
 *
 * Logic for damage calculation is in LegacyWeapon.
 */
public class TwoHandedTrait extends ModifierWithBadge {

  public static final String ID = "legacy:two_handed";

  public TwoHandedTrait() {
    super(ID);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new TwoHandedTrait();
  }

}
