package legacy;

import com.badlogic.gdx.Game;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import legacy.cards.Anathema;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Tracks all permanent changes for our cards.
 */
public class LegacyDb {

  private static final String db_path = SpireConfig.makeFilePath("legacy", "permanent_settings", "db");
  private static final String connection_string = "jdbc:sqlite:" + db_path;

  private Map<String, Integer> changeSet;

  public LegacyDb() {
    // I have NO idea why, but it's necessary to try and hotload this class first before accesing the DB in order
    // to make sure that it loads correctly. WTF.
    try {
      Class cls = Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    this.changeSet = new HashMap<String, Integer>();
  }

  // Initializes the sqlite database connection. Creates the db file if it doesn't exist, as well as the base tables.
  public void initialize() {
    try (Connection connection = DriverManager.getConnection(connection_string)) {
      if (connection != null) {
        DatabaseMetaData meta = connection.getMetaData();
        System.out.println("The driver name is: " + meta.getDriverName());
        String sql = "CREATE TABLE IF NOT EXISTS cards (cardId text PRIMARY KEY, damage int);";
        Statement stmt = connection.createStatement();
        stmt.execute(sql);

        // Insert base damage for anathema.
        // String sql2 = "INSERT INTO cards (cardId, damage) VALUES ('legacy:anathema', 15);";
        // Statement stmt2 = connection.createStatement();
        // stmt2.execute(sql2);
      }
    } catch (SQLException e) {
      System.out.println("It's java you fucking idiot");
      System.out.println(e.getMessage());
    }
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
    }

    // Sus.
    CardLibrary.add(new Anathema());
  }

  public String getName(String cardId, String baseName) {
    return "+" + getCardDamage(cardId) + " adamntine flaming holy speedy " + baseName;
  }

}
