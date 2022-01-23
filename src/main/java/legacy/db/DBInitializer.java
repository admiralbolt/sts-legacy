package legacy.db;

import legacy.cards.LegacyCards;
import legacy.enchantments.Enchantment;
import legacy.enchantments.EnchantmentsManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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

  private static final String CARD_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + LegacyDb.CARDS_TABLE + " (cardId text PRIMARY KEY, damage int, numUpgrades int);";
  private static final String ENCHANTMENT_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + LegacyDb.ENCHANTMENTS_TABLE + " (enchantmentId text PRIMARY KEY, name text, description text);";
  private static final String CARD_ENCHANTMENT_JOIN_SQL = "CREATE TABLE IF NOT EXISTS " + LegacyDb.CARD_ENCHANTMENT_JOIN_TABLE + " (cardId text, enchantmentId text, FOREIGN KEY(cardId) REFERENCES cards(cardId), FOREIGN KEY(enchantmentId) REFERENCES enchantments(enchantmentId));";

  // Initializes default database values if they do not exist.
  // All of these methods are insert or ignore / create if not exists.
  public static void initialize() {
    try (Connection connection = DriverManager.getConnection(LegacyDb.CONNECTION_STRING)) {
      if (connection != null) {
        Statement stmt = connection.createStatement();
        // Create tables.
        stmt.addBatch(CARD_TABLE_SQL);
        stmt.addBatch(ENCHANTMENT_TABLE_SQL);
        stmt.addBatch(CARD_ENCHANTMENT_JOIN_SQL);

        // Insert base data.
        stmt.addBatch(getCardDataSQL());
        stmt.addBatch(getEnchantmentDataSQL());

        stmt.executeBatch();
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public static String getCardDataSQL() {
    return "INSERT OR IGNORE INTO cards (cardId, damage, numUpgrades) VALUES " + LegacyCards.getAllCardsDbInfo().stream().map(DBCardInfo::toDatabaseString).collect(Collectors.joining(", ")) + ";";
  }

  public static String getEnchantmentDataSQL() {
    return "INSERT OR IGNORE INTO enchantments (enchantmentId, name, description) VALUES " + EnchantmentsManager.allEnchantments.stream().map(Enchantment::toDatabaseString).collect(Collectors.joining(", ")) + ";";
  }


}
