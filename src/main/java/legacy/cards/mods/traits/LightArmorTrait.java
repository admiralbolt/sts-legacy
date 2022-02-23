package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import legacy.cards.mods.ModifierWithBadge;

/**
 * Light armor scales normally with dex. Playing light armor cards grant 1 Temporary Dexterity.
 */
public class LightArmorTrait extends ModifierWithBadge {

  public static final String ID = "legacy:light_armor";

  public LightArmorTrait() {
    super(ID);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new LightArmorTrait();
  }
}
