package legacy.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

import java.util.ArrayList;

/**
 * Increases the damage of ALL spire enemies by 25% per act, reaching 100% at act 4.
 */
@SpirePatch(clz=AbstractMonster.class, method="calculateDamage", paramtypez={int.class})
public class IncreaseEnemyDamagePatch {

  @SpireInsertPatch(locator=Locator.class, localvars={"tmp"})
  public static void Insert(AbstractMonster __instance, @ByRef float[] tmp) {
    tmp[0] = tmp[0] * (1 + 0.25f * AbstractDungeon.actNum);
  }

  private static class Locator extends SpireInsertLocator {
    @Override
    public int[] Locate(CtBehavior ctBehavior) throws Exception {
      // Should match the following line in AbstractMonster.class:
      // if (Settings.isEndless && AbstractDungeon.player.hasBlight("DeadlyEnemies")) {
      Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasBlight");

      return LineFinder.findInOrder(ctBehavior, new ArrayList<>(), finalMatcher);
    }
  }

}
