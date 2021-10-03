package legacy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A helper class for tracking default values of cards for database instantiation.
 *
 * There's a slightly annoying circular dependency here: We need base values of
 * cards in order to properly initialize the database. We need values from the
 * database in order to properly construct an instance of a card.
 * CardStrings contains the proper name / description which we can store in the DB,
 * but other values will need to be tracked here in order to properly initialize.
 */
public class DBInitializer {

  public static class DBCardInfo {

    public String id;
    public int damage;

    public DBCardInfo(String id, int damage) {
      this.id = id;
      this.damage = damage;
    }

    public String toDatabaseString() {
      return String.format("('%s', %d)", id, damage);
    }
  }

  public static List<DBCardInfo> getCards() {
    List<DBCardInfo> cards = new ArrayList<>();
    cards.add(new DBCardInfo("legacy:anathema", 15));
    return cards;
  }

  public static String getInsertString() {
    return "INSERT OR IGNORE INTO cards (cardId, damage) VALUES " + getCards().stream().map(DBCardInfo::toDatabaseString).collect(Collectors.joining(", ")) + ";";
  }


}
