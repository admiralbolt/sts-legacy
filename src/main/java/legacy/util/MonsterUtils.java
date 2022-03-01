package legacy.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.patches.MonsterXpPatch;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class MonsterUtils {

  public static enum MonsterType {
    ABERRATION,
    BEAST,
    BIRD,
    CONSTRUCT,
    GREMLIN,
    HUMANOID,
    SLIME
  }

  // Each monster is worth a certain amount of xp. Minions are not worth anything! :(
  public static Map<String, Integer> MONSTER_XP;

  // Each monster has particular types associated with it.
  public static Map<String, Set<MonsterType>> MONSTER_TYPES;

  public static void postInitialize() {
    MONSTER_XP = new HashMap<>();
    MONSTER_TYPES = new HashMap<>();

    // Load monsters from json.
    Type mapType = new TypeToken<Map<String, Map<String, Object>>>(){}.getType();
    FileHandle handle = Gdx.files.internal("legacy/monsters.json");
    Map<String, Map<String, Object>> data = new Gson().fromJson(handle.readString(), mapType);
    data.forEach((m, info) -> {
      List<String> rawTypes = (List<String>) info.getOrDefault("types", new ArrayList<String>());
      Set<MonsterType> types = rawTypes.stream().map(MonsterType::valueOf).collect(Collectors.toSet());
      MONSTER_TYPES.put(m, types);
      MONSTER_XP.put(m, (int) (double) info.getOrDefault("xp", 0));
    });
  }

  // Get the correct amount of xp for a monster.
  public static int getXp(AbstractMonster m) {
    if (!MonsterXpPatch.WorthXpField.worthXp.get(m)) return 0;

    return MONSTER_XP.getOrDefault(m.id, 0);
  }

  // Check if a monster is the expected type.
  public static boolean isMonsterType(AbstractMonster m, MonsterType type) {
    return MONSTER_TYPES.getOrDefault(m.id, new HashSet<>()).contains(type);
  }

  public static MonsterType getRandomType() {
    MonsterType[] types = MonsterType.values();
    return types[CardUtils.RANDOM.nextInt(types.length)];
  }
}
