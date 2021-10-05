package legacy.db;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import legacy.cards.LegacyCards;
import legacy.cards.weapons.Anathema;
import legacy.cards.weapons.Rapier;
import legacy.enchantments.Enchantment;
import legacy.enchantments.EnchantmentsManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tracks all permanent changes for our cards.
 */
public class LegacyDb {

  public static final String CARDS_TABLE = "cards";
  public static final String ENCHANTMENTS_TABLE = "enchantments";
  public static final String CARD_ENCHANTMENT_JOIN_TABLE = "enchantments_cards_join";

  // So that I don't have to dig in the future:
  // Mac     - /Users/<username>/Library/Preferences/ModTheSpire/legacy/
  // Windows - /mnt/c/Users/<username>/AppData/Local/ModTheSpire/legacy/
  private static final String DB_PATH = SpireConfig.makeFilePath("legacy", "legacy", "db");
  public static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_PATH;

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

  /**
   * Gets all the enchantments attached to a card.
   */
  public List<Enchantment> loadCardEnchantments(String cardId) {
    List<Enchantment> enchantments = new ArrayList<>();
    try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
      String sql = "SELECT enchantmentId FROM " + CARD_ENCHANTMENT_JOIN_TABLE + " WHERE cardId = ?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, cardId);
      ResultSet result = stmt.executeQuery();
      while (result.next()) {
        String enchantmentId = result.getString("enchantmentId");
        System.out.println(enchantmentId);
        Enchantment enchantment = EnchantmentsManager.getEnchantment(enchantmentId);
        if (enchantment == null) {
          System.out.println("Could not find enchantment with id: " + enchantmentId);
          continue;
        }

        enchantments.add(enchantment);
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return enchantments;
  }

  public DBCardInfo getCardInfo(String cardId) {
    try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
      String sql = "SELECT damage, numUpgrades FROM " + CARDS_TABLE + " WHERE cardId = ?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, cardId);
      ResultSet result = stmt.executeQuery();
      result.next();
      return new DBCardInfo(cardId, result.getInt("damage"), result.getInt("numUpgrades"));
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  /**
   * Upgrades a card and persists the value to the database.
   */
  public void upgradeCard(String cardId) {
    DBCardInfo info = getCardInfo(cardId);
    try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
      String sql = "UPDATE " + CARDS_TABLE + " SET damage = ?, numUpgrades = ? WHERE cardId = ?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, info.damage + 1);
      stmt.setInt(2,info.numUpgrades + 1);
      stmt.setString(3, cardId);
      stmt.execute();

      CardLibrary.add(LegacyCards.getInstanceById(cardId));
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void queueChange(String cardId, int damage) {
    this.changeSet.put(cardId, damage);
  }

  // Commits changes that have been built up over the course of a single combat to the database.
  public void commitChanges() {
    try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
      String sql = "UPDATE " + CARDS_TABLE + " SET damage = ? WHERE cardId = ?";
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

  /**
   * Returns a nicely formatted cardname i.e.:
   * +2 Corrosive Rapier
   */
  public String getName(String cardId, String baseName) {
    StringBuilder builder = new StringBuilder();
    DBCardInfo info = getCardInfo(cardId);
    if (info.numUpgrades > 0) {
      builder.append("+");
      builder.append(info.numUpgrades);
      builder.append(" ");
    }

    List<Enchantment> enchantments = loadCardEnchantments(cardId);
    for (Enchantment enchantment : enchantments) {
      builder.append(enchantment.name);
      builder.append(" ");
    }

    builder.append(baseName);
    return builder.toString();
  }

  public String getCardDescription(String cardId, String baseDescription) {
    StringBuilder builder = new StringBuilder();
    builder.append(baseDescription);

    List<Enchantment> enchantments = loadCardEnchantments(cardId);
    for (Enchantment enchantment : enchantments) {
      builder.append(" NL ");
      builder.append(enchantment.description);
    }

    return builder.toString();
  }

}
