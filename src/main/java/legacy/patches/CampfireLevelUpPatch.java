package legacy.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import legacy.characters.TheAdventurer;
import legacy.ui.LevelUpCampfireOption;
import org.apache.logging.log4j.core.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

@SpirePatch(clz=CampfireUI.class, method="initializeButtons")
public class CampfireLevelUpPatch {

  @SpirePrefixPatch
  public static void Prefix(CampfireUI __instance) {
    addLevelUpOption(__instance);
  }

  public static void addLevelUpOption(CampfireUI instance) {
    if (!(AbstractDungeon.player instanceof TheAdventurer)) return;
    TheAdventurer adventurer = (TheAdventurer) AbstractDungeon.player;

    try {
      Field field = instance.getClass().getDeclaredField("buttons");
      Object buttonArray = ReflectionUtil.getFieldValue(field, instance);
      if (!(buttonArray instanceof ArrayList)) return;

      ArrayList<AbstractCampfireOption> buttons = (ArrayList<AbstractCampfireOption>) buttonArray;
      LevelUpCampfireOption option = new LevelUpCampfireOption();

      // Level Up should only be available if the player actually has enough XP to do so.
      if (adventurer.xp < adventurer.nextLevelXp) option.usable = false;

      buttons.add(option);

    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

}
