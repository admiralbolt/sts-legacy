package legacy.patches;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder;
import javassist.CtBehavior;
import legacy.util.ReflectionUtils;

import java.util.ArrayList;

/**
 * There's a really annoying bug here. When adding a new enchantment to a card,
 * it will get set to "glowing", which will cause it to crash when you try
 * to open in the library. This is due to a bad check in the AbstractCard
 * render border code. This is really minor, but I'm gonna fix it anyway.
 */
@SpirePatch(clz = CardGlowBorder.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, Color.class})
public class FixCardGlowBorderCrashPatch {

  @SpireInsertPatch(locator = Locator.class)
  public static SpireReturn<CardGlowBorder> Insert(CardGlowBorder __instance, AbstractCard card, Color color) {
    if (AbstractDungeon.currMapNode == null) {
      ReflectionUtils.setFieldValue(__instance, "color", Color.GREEN.cpy());
      return SpireReturn.Return(__instance);
    }
    return SpireReturn.Continue();
  }

  private static class Locator extends SpireInsertLocator {
    @Override
    public int[] Locate(CtBehavior ctBehavior) throws Exception {
      // Should match the following line in CardGlowBorder.class:
      // if (AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
      Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCurrRoom");

      return LineFinder.findInOrder(ctBehavior, new ArrayList<>(), finalMatcher);
    }
  }

}
