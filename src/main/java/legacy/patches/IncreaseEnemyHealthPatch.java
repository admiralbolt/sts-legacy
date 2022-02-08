package legacy.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

import java.util.ArrayList;

/**
 * Increases the health of ALL spire enemies by 50% per act, reaching 200% at act 4.
 *
 * As an aside, the @SpirePatch business is still ridiculously cool black magic.
 */
@SpirePatch(clz=AbstractMonster.class, method="setHp", paramtypez={int.class, int.class})
public class IncreaseEnemyHealthPatch {

  @SpireInsertPatch(locator=Locator.class)
  public static void Insert(AbstractMonster instance) {
    instance.currentHealth = (int)((float) instance.currentHealth * (1 + 0.5F * AbstractDungeon.actNum));
  }

  private static class Locator extends SpireInsertLocator {
    @Override
    public int[] Locate(CtBehavior ctBehavior) throws Exception {
      // Should match the following line in AbstractMonster.class:
      // if (Settings.isEndless && AbstractDungeon.player.hasBlight("ToughEnemies")) {
      Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasBlight");

      return LineFinder.findInOrder(ctBehavior, new ArrayList<Matcher>(), finalMatcher);
    }
  }

}
