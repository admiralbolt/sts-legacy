package legacy.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import legacy.LegacyMod;

/**
 * Save permanent changes when we save the game.
 */
public class SavePermanentChangesPatch {

  @SpirePatch(clz=SaveAndContinue.class, method="save")
  public static class SaveGame {
    public static void Prefix() {
      LegacyMod.saveCharacterStats();
      LegacyMod.LEGACY_DB.commitChanges();
    }
  }


}
