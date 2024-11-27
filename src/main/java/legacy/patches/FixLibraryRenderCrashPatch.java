package legacy.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * There's a really annoying bug here. When adding a new enchantment to a card,
 * it will get set to "glowing", which will cause it to crash when you try
 * to open in the library. This is due to a bad check in the AbstractCard
 * render border code. This is really minor, but I'm gonna fix it anyway.
 */
@SpirePatch(clz = AbstractCard.class, method = "renderMainBorder")
public class FixLibraryRenderCrashPatch {

  @SpirePrefixPatch
  public static SpireReturn<Void> Prefix(AbstractCard card, SpriteBatch sb) {
    if (AbstractDungeon.currMapNode == null) {
      return SpireReturn.Return();
    }
    return SpireReturn.Continue();
  }

}
