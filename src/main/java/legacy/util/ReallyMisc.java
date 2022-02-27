package legacy.util;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.stream.Stream;

public class ReallyMisc {

  public static Stream<AbstractMonster> getAllEnemies() {
    return AbstractDungeon.getCurrRoom().monsters.monsters.stream().filter(m -> !m.isDeadOrEscaped());
  }

}
