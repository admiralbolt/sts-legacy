package legacy.db;

import java.util.ArrayList;
import java.util.List;

public class DBCardInfo {

  public String id;
  public int damage;
  public int numUpgrades = 0;

  public DBCardInfo(String id, int damage) {
    this.id = id;
    this.damage = damage;
  }

  public DBCardInfo(String id, int damage, int numUpgrades) {
    this.id = id;
    this.damage = damage;
    this.numUpgrades = numUpgrades;
  }

  public String toDatabaseString() {
    return String.format("('%s', %d, %d)", id, damage, numUpgrades);
  }

  public static List<DBCardInfo> getAllCards() {
    List<DBCardInfo> cards = new ArrayList<>();
    cards.add(new DBCardInfo("legacy:anathema", 15));
    cards.add(new DBCardInfo("legacy:dagger", 2));
    cards.add(new DBCardInfo("legacy:longsword", 6));
    cards.add(new DBCardInfo("legacy:mace", 5));
    cards.add(new DBCardInfo("legacy:rapier", 4));
    cards.add(new DBCardInfo("legacy:whip", 2));
    return cards;
  }
}
