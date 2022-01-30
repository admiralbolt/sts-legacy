package legacy.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import legacy.characters.TheAdventurer;
import org.apache.logging.log4j.core.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * We want monsters to give xp on death.
 *
 * This can be done easily by patching the AbstractMonster die() method. However, some monsters die of their own
 * accord: Slimes splitting, exploders exploding, transient fading e.t.c. We want to make sure that an enemy didn't
 * die from a suicide action. To do that, we need an extra flag on AbstractMonster that determines if it's worth xp
 * or not, and gets flipped if the SuicideAction() is called.
 */
public class MonsterXpPatch {

  // Hook enemies death into our gain xp function.
  @SpirePatch(clz = AbstractMonster.class, method = "die", paramtypez = {})
  public static class MonstersGiveXpOnDeath {

    @SpirePrefixPatch
    public static void Prefix(AbstractMonster __instance) {
      if (AbstractDungeon.player instanceof TheAdventurer) {
        ((TheAdventurer) AbstractDungeon.player).gainXp(__instance);
      }
    }
  }

  // Add a field to AbstractMonster 'worthXp' that's a boolean that determines if a monster is 'worthXp' or not.
  @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CLASS)
  public static class WorthXpField {
    public static SpireField<Boolean> worthXp = new SpireField<>(() -> true);
  }

  // If a monster triggers the suicide action it *shouldn't* give xp.
  @SpirePatch(clz=SuicideAction.class, method="update")
  public static class MonstersGiveNoXpOnSuicide {

    @SpireInsertPatch(locator = Locator.class)
    public static void Insert(SuicideAction __instance) {
      try {
        Field field = __instance.getClass().getField("m");
        Object monster = ReflectionUtil.getFieldValue(field, __instance);
        WorthXpField.worthXp.set(monster, false);
      } catch (NoSuchFieldException e) {
        System.out.println("GAHHHHH");
        e.printStackTrace();
      }
    }

    public static class Locator extends SpireInsertLocator {
      @Override
      public int[] Locate(CtBehavior ctBehavior) throws Exception {
        // Should match the following line in SuicideAction.class:
        // this.m.die(this.relicTrigger);
        Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "die");

        return LineFinder.findInOrder(ctBehavior, new ArrayList<>(), finalMatcher);
      }
    }
  }

}
