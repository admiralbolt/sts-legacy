package legacy.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.helpers.BlightHelper;
import legacy.base_classes.Fighter;
import legacy.base_classes.Rogue;
import legacy.base_classes.Wizard;

@SpirePatch(clz=BlightHelper.class, method="getBlight")
public class BaseClassBlightPatch {

  @SpirePrefixPatch
  public static SpireReturn<AbstractBlight> Prefix(String id) {
    if (Fighter.ID.equals(id)) {
      return SpireReturn.Return(new Fighter(1));
    } else if (Rogue.ID.equals(id)) {
      return SpireReturn.Return(new Rogue(1));
    } else if (Wizard.ID.equals(id)) {
      return SpireReturn.Return(new Wizard(1));
    }

    return SpireReturn.Continue();
  }
}
