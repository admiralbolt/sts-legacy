package legacy.enchantments;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentsManager {

  public static List<Enchantment> allEnchantments = new ArrayList<Enchantment>(){{
    add(new Corrosive());
  }};
}
