package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import legacy.cards.mods.ModifierWithBadge;

/**
 * Ranged weapons ignore block, thorns, curl up, malleable, and sharp hide.
 *
 * Logic for damage calculation is done in PiercingDamageAction.
 */
public class RangedTrait extends ModifierWithBadge {

  public static final String ID = "legacy:ranged";

  public RangedTrait() {
    super(ID);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new RangedTrait();
  }

}
