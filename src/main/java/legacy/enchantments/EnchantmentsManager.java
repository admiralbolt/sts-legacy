package legacy.enchantments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tracks all enchantments.
 */
public class EnchantmentsManager {

  public static List<Enchantment> allEnchantments;
  public static Map<String, Enchantment> enchantmentMap;

  public static void addEnchantment(Enchantment enchantment) {
    allEnchantments.add(enchantment);
    enchantmentMap.put(enchantment.id, enchantment);
  }

  public static void initialize() {
    allEnchantments = new ArrayList<>();
    enchantmentMap = new HashMap<>();
    addEnchantment(new Corrosive());
  }

  public static Enchantment getEnchantment(String enchantmentId) {
    return enchantmentMap.getOrDefault(enchantmentId, null);
  }

}
