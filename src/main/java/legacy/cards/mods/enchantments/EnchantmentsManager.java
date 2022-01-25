package legacy.cards.mods.enchantments;

import basemod.helpers.CardBorderGlowManager;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;

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

    // DON'T FORGET TO ADD ENCHANTMENTS HERE!
    addEnchantment(new Corrosive());
    addEnchantment(new Icy());
  }

  public static void postInitialize() {
    for (Enchantment enchantment : allEnchantments) {
      CardBorderGlowManager.addGlowInfo(new CardBorderGlowManager.GlowInfo() {

        @Override
        public boolean test(AbstractCard card) {
          return CardModifierManager.hasModifier(card, enchantment.id);
        }

        @Override
        public Color getColor(AbstractCard card) {
          return enchantment.getColor();
        }

        @Override
        public String glowID() {
          return enchantment.id + "_glow";
        }
      });
    }
  }

  public static Enchantment getEnchantment(String enchantmentId) {
    return enchantmentMap.getOrDefault(enchantmentId, null);
  }

}
