package legacy.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import legacy.LegacyMod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class RelicUtils {

  // Relic charges! This is permanent charge counters for relics.
  public static SpireConfig RELIC_CHARGES;

  public static void initialize() {
    Properties defaults = new Properties();
    try {
      RELIC_CHARGES = new SpireConfig(LegacyMod.MOD_ID, "relic_charges", defaults);
      RELIC_CHARGES.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void saveRelicCharges() {
    try {
      RELIC_CHARGES.save();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Gets the charge for a particular relic. If an entry doesn't exist, set it
  // to 1 charge in the config map.
  public static Integer getCharge(String relicId) {
    if (!RELIC_CHARGES.has(relicId)) {
      System.out.println("NO CHARGES ENCOUNTERED FOR: " + relicId);
      RELIC_CHARGES.setInt(relicId, 1);
      return 1;
    }

    return RELIC_CHARGES.getInt(relicId);
  }

  // Increment the charge count for a relic.
  public static void incrementCharge(String relicId) {
    if (!RELIC_CHARGES.has(relicId)) {
      System.out.println("Shouldn't really be calling increment on a new relic but :shrug:");
      RELIC_CHARGES.setInt(relicId, 1);
      return;
    }

    RELIC_CHARGES.setInt(relicId, RELIC_CHARGES.getInt(relicId) + 1);
  }

}
