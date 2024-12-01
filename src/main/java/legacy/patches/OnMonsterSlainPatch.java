package legacy.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import javassist.CtBehavior;
import legacy.powers.LegacyThieveryPower;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Make sure we get our gold when we kill a monster with thievery :)
 */
@SpirePatch(clz = AbstractMonster.class, method = "damage", paramtypez = {DamageInfo.class})
public class OnMonsterSlainPatch {

  @SpireInsertPatch(locator = Locator.class)
  public static SpireReturn<Void> Insert(AbstractMonster __instance, DamageInfo damageInfo) {
    AbstractPower power = AbstractDungeon.player.getPower(LegacyThieveryPower.POWER_ID);
    int thieveryAmount = (power != null) ? power.amount : 0;
    if (damageInfo.type == DamageInfo.DamageType.NORMAL && thieveryAmount > 0 && AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
      // Gain gold directly, if we try to use an action it'll be cleared since monsters are basically dead.
      AbstractDungeon.player.gainGold(thieveryAmount);
      // Also, we don't need to add a penny effect here, because effects aren't cleared. Just game actions.
    }

    return SpireReturn.Continue();
  }

  private static class Locator extends SpireInsertLocator {
    @Override
    public int[] Locate(CtBehavior ctBehavior) throws Exception {
      // Should match the following line in AbstractMonster.class:
      // if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
      Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getMonsters");

      return LineFinder.findInOrder(ctBehavior, new ArrayList<>(), finalMatcher);
    }
  }

}
