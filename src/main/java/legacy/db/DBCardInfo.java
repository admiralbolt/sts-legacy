package legacy.db;

/**
 * Card info stored in the DB. This is:
 * - the "value" of the card aka the damage or block.
 * - the number of times the card has been upgraded.
 */
public class DBCardInfo {

  public String id;
  public int value;
  public int numUpgrades = 0;

  public DBCardInfo(String id, int value) {
    this.id = id;
    this.value = value;
  }

  public DBCardInfo(String id, int value, int numUpgrades) {
    this.id = id;
    this.value = value;
    this.numUpgrades = numUpgrades;
  }

  public String toDatabaseString() {
    return String.format("('%s', %d, %d)", id, value, numUpgrades);
  }
}
