package legacy.util;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.*;
import legacy.patches.MonsterXpPatch;

import java.util.HashMap;
import java.util.Map;

public class MonsterUtils {

  public static enum MonsterType {
    ABBERATION,
    BEAST,
    BIRD,
    CONSTRUCT,
    GREMLIN,
    HUMANOID,
    SLIME
  }

  // Each monster is worth a certain amount of xp. Minions are not worth anything! :(
  public static Map<String, Integer> MONSTER_XP;

  // Each monster has a particular type.
  public static Map<String, MonsterType> MONSTER_TYPES;

  public static void addMonster(String id, MonsterType type, Integer xp) {
    MONSTER_XP.put(id, xp);
    MONSTER_TYPES.put(id, type);
  }

  public static void initialize() {
    MONSTER_XP = new HashMap<>();
    MONSTER_TYPES = new HashMap<>();

    // ALL THE MONSTERS.
    addMonster(JawWorm.ID, MonsterType.BEAST, 16);
    addMonster(LouseDefensive.ID, MonsterType.BEAST, 5);
    addMonster(LouseNormal.ID, MonsterType.BEAST, 5);
    addMonster(Cultist.ID, MonsterType.HUMANOID, 13);

    // THERE ARE SO MANY GOD DAMN SLIMES IN THIS GAME.
    // Managing to kill one of the bigger slimes is worth more xp than the little guys.
    addMonster(AcidSlime_S.ID, MonsterType.SLIME, 6);
    addMonster(AcidSlime_M.ID, MonsterType.SLIME, 11);
    addMonster(AcidSlime_L.ID, MonsterType.SLIME, 25);
    addMonster(SpikeSlime_S.ID, MonsterType.SLIME, 6);
    addMonster(SpikeSlime_M.ID, MonsterType.SLIME, 11);
    addMonster(SpikeSlime_L.ID, MonsterType.SLIME, 25);
    addMonster(SlimeBoss.ID, MonsterType.SLIME, 60);
  }

  // Get the correct amount of xp for a monster.
  public static int getXp(AbstractMonster m) {
    if (!MonsterXpPatch.WorthXpField.worthXp.get(m)) return 0;

    return MONSTER_XP.getOrDefault(m.id, 0);
  }
}
