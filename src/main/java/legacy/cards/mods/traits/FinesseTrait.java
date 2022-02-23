package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import legacy.cards.mods.ModifierWithBadge;

/**
 * Two handed weapons have +1x scaling with strength.
 */
public class FinesseTrait extends ModifierWithBadge {

  public static final String ID = "legacy:finesse";

  public FinesseTrait() {
    super(ID);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new FinesseTrait();
  }


}
