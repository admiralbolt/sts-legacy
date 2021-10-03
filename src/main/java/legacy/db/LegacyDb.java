package legacy.db;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import legacy.cards.Anathema;
import legacy.enchantments.Enchantment;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tracks all permanent changes for our cards.
 */
public class LegacyDb {

  private static final String db_path = SpireConfig.makeFilePath("legacy", "legacy", "db");
  public static final String connection_string = "jdbc:sqlite:" + db_path;

  private Map<String, Integer> changeSet;

  public LegacyDb() {
    // I have NO idea why, but it's necessary to try and hotload this class first
    // before accessing the DB in order to make sure that it loads correctly. WTF.
    try {
      Class<?> cls = Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    this.changeSet = new HashMap<>();
  }

  /**
   * Initialize the database.
   * Creates the file if it doesn't exist, as well as default values for
   * each table. This is broken out into a separate class for sanity.
   */
  public void initialize() {
    DBInitializer.initialize();
  }

  public List<Enchantment> loadCardEnchantments(String cardId) {
    return new ArrayList<>();
  }

  // Gets damage for a particular card.
  public int getCardDamage(String cardId) {
    try (Connection connection = DriverManager.getConnection(connection_string)) {
      String sql = "SELECT damage from cards WHERE cardId = ?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, cardId);
      ResultSet result = stmt.executeQuery();
      result.next();
      return result.getInt("damage");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return 0;
  }

  public void queueChange(String cardId, int damage) {
    this.changeSet.put(cardId, damage);
  }

  // Commits changes that have been built up over the course of a single combat to the database.
  public void commitChanges() {
    try (Connection connection = DriverManager.getConnection(connection_string)) {
      String sql = "UPDATE cards SET damage = ? WHERE cardId = ?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      for (Map.Entry<String, Integer> entry : this.changeSet.entrySet()) {
        stmt.setInt(1, entry.getValue());
        stmt.setString(2, entry.getKey());
        stmt.execute();
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    } finally {
      this.changeSet.clear();
    }

    // Sus.
    CardLibrary.add(new Anathema());
  }

  public String getName(String cardId, String baseName) {
    return "+" + getCardDamage(cardId) + " adamntine flaming holy speedy " + baseName;
  }

}
