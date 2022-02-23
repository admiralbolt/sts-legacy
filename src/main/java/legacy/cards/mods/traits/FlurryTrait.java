package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import legacy.cards.mods.ModifierWithBadge;

/**
 * Flurry weapons deal more damage the more you've played in a single  turn.
 *
 * Logic for damage calculation is in LegacyWeapon.
 */
public class FlurryTrait extends ModifierWithBadge {

  public static final String ID = "legacy:flurry";

  public FlurryTrait() {
    super(ID);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new FlurryTrait();
  }

}
