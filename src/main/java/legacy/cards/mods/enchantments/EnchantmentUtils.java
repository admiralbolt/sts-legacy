package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardBorderGlowManager;
import basemod.helpers.CardModifierManager;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.CardModifierPatches;
import basemod.patches.com.megacrit.cardcrawl.saveAndContinue.SaveFile.ModSaves;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.ClassPool;
import legacy.LegacyMod;
import legacy.cards.LegacyCard;
import legacy.cards.equipment.armor.LegacyArmor;
import legacy.cards.equipment.weapons.LegacyWeapon;
import org.clapper.util.classutil.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Tracks all enchantments.
 */
public class EnchantmentUtils {

  // Card enchantments! These are all enchantments applied to a card.
  public static SpireConfig CARD_ENCHANTMENTS;

  public static List<Enchantment> allEnchantments;
  public static Map<String, Enchantment> enchantmentMap;
  public static Map<LegacyCard.LegacyCardType, EnchantmentPool> enchantmentPools;

  public static Map<AbstractCard.CardRarity, Integer> MAX_ENCHANTMENTS_BY_RARITY;

  // Enchantments that have been saved permanently to the config file.
  public static Map<String, List<AbstractCardModifier>> PERSISTED_ENCHANTMENTS;

  public static Set<String> EXCLUDE_ENCHANTMENTS = new HashSet<>(Arrays.asList(
          ArmorPlusX.class.getName(),
          Bane.class.getName(),
          WeaponPlusX.class.getName())
  );

  public static void initialize() {
    Properties defaults = new Properties();
    defaults.setProperty("cards", "{}");
    try {
      CARD_ENCHANTMENTS = new SpireConfig(LegacyMod.MOD_ID, "card_enchantments", defaults);
      CARD_ENCHANTMENTS.load();
      loadEnchantments();
    } catch (IOException e) {
      e.printStackTrace();
    }

    allEnchantments = new ArrayList<>();
    enchantmentMap = new HashMap<>();
    enchantmentPools = new HashMap<>();
    enchantmentPools.put(LegacyCard.LegacyCardType.ARMOR, new EnchantmentPool());
    enchantmentPools.put(LegacyCard.LegacyCardType.WEAPON, new EnchantmentPool());
    // Each rarity can have a certain number of enchantments.
    MAX_ENCHANTMENTS_BY_RARITY = new HashMap<>();
    MAX_ENCHANTMENTS_BY_RARITY.put(AbstractCard.CardRarity.UNCOMMON, 3);
    MAX_ENCHANTMENTS_BY_RARITY.put(AbstractCard.CardRarity.RARE, 4);

    // Probably overkill, but I'm lazy and would rather dynamically load enchantments.
    // Don't want abstract classes or interfaces.
    List<ClassFilter> filters = new ArrayList<>(Arrays.asList(new NotClassFilter(new InterfaceOnlyClassFilter()), new NotClassFilter(new AbstractClassFilter()), new ClassModifiersClassFilter(1)));
    ClassFilter filter = new AndClassFilter(filters.toArray(new ClassFilter[0]));
    Collection<ClassInfo> foundClasses = new ArrayList<>();
    ClassFinder finder = new ClassFinder();
    // Make sure we actually add a file to search for enchantments.
    for (ModInfo info : Loader.MODINFOS) {
      if (info == null || info.jarURL == null || !LegacyMod.MOD_ID.equals(info.ID)) continue;

      try {
        finder.add(new File(info.jarURL.toURI()));
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    }

    finder.findClasses(foundClasses, filter);
    ClassPool pool = Loader.getClassPool();
    String className = Enchantment.class.getName();

    for (ClassInfo info : foundClasses) {
      if (!className.equals(info.getClassName()) && !className.equals(info.getSuperClassName())) continue;
      if (EXCLUDE_ENCHANTMENTS.contains(info.getClassName())) continue;

      try {
        Enchantment e = (Enchantment) pool.getClassLoader().loadClass(info.getClassName()).newInstance();
        allEnchantments.add(e);
        enchantmentMap.put(e.id, e);
        enchantmentPools.get(e.type).addEnchantment(e);
      } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  // We want our enchanted cards to glow to match their enchantments color :)
  public static void postInitialize() {
    for (Enchantment enchantment : allEnchantments) {
      if (enchantment.getColor() == null) continue;

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

  // Get all the enchantments currently on a card.
  public static List<Enchantment> getCardEnchantments(AbstractCard card) {
    return CardModifierManager.modifiers(card).stream()
            .filter(mod -> mod instanceof Enchantment)
            .map(mod -> (Enchantment) mod)
            .collect(Collectors.toList());
  }

  // Gets the maximum number of enchantments a card can have.
  public static int getMaxEnchantments(AbstractCard card) {
    return MAX_ENCHANTMENTS_BY_RARITY.getOrDefault(card.rarity, 2);
  }

  // Whether or not a card can be enchanted.
  // 1. Should be a LegacyCard.
  // 2. Should have enchantable = true.
  // 3. Shouldn't have max number of enchantments already.
  public static boolean canEnchant(AbstractCard card) {
    if (!(card instanceof LegacyCard)) return false;

    if (!((LegacyCard) card).enchantable) return false;

    // Temporary check since I only have weapon enchantments currently.
    if (!(card instanceof LegacyWeapon) && !(card instanceof LegacyArmor)) return false;

    // Make sure the card isn't fully enchanted.
    List<Enchantment> enchantments = getCardEnchantments(card);
    return enchantments.size() < getMaxEnchantments(card);
  }

  // Get random enchantments for a card!
  public static List<Enchantment> randomEnchantments(LegacyCard card, int amount) {
    return enchantmentPools.get(card.legacyCardType).rollEnchantments(card, amount);
  }

  public static List<LegacyCard> getEnchantableCards() {
    return AbstractDungeon.player.masterDeck.group.stream()
            .filter(EnchantmentUtils::canEnchant)
            .map(c -> (LegacyCard) c)
            .collect(Collectors.toList());
  }

  // Serialize the enchantments from the master deck, and saves them as a string property on the card enchantments
  // spire field. We want our final form to be a mapping from a card id -> a list of enchantments on that card.
  // Logic is *very* similar to the ConstructSaveFilePatch.java.
  public static void commitEnchantments() {
    ModSaves.HashMapOfJsonElement enchantmentMap = new ModSaves.HashMapOfJsonElement();

    GsonBuilder builder = new GsonBuilder();
    if (CardModifierPatches.modifierAdapter == null) {
      CardModifierPatches.initializeAdapterFactory();
    }
    builder.registerTypeAdapterFactory(CardModifierPatches.modifierAdapter);
    Gson gson = builder.create();

    // Iterate through cards in the master deck, and find all modifiers that are enchantments.
    for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
      List<AbstractCardModifier> enchantmentMods = CardModifierManager.modifiers(card).stream()
              .filter(mod -> mod instanceof Enchantment)
              .collect(Collectors.toList());

      if (enchantmentMods.isEmpty()) continue;

      // Json black magic I don't need or want to understand.
      enchantmentMap.put(card.cardID, gson.toJsonTree(enchantmentMods, new TypeToken<ArrayList<AbstractCardModifier>>(){}.getType()));
    }

    CARD_ENCHANTMENTS.setString("cards", gson.toJson(enchantmentMap));
  }

  // Utilizes the above, but also persists the enchantments to disk.
  public static void saveEnchantments() {
    commitEnchantments();
    try {
      CARD_ENCHANTMENTS.save();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Load our previously saved enchantments from the config file. Make sure CARD_ENCHANTMENTS is initialized first.
  // These values get saved into the PERSISTED_ENCHANTMENTS hash map.
  // Logic is *very* similar to the LoadPlayerSaves.java.
  public static void loadEnchantments() {
    PERSISTED_ENCHANTMENTS = new HashMap<>();
    GsonBuilder builder = new GsonBuilder();
    if (CardModifierPatches.modifierAdapter == null) {
      CardModifierPatches.initializeAdapterFactory();
    }

    builder.registerTypeAdapterFactory(CardModifierPatches.modifierAdapter);
    Gson gson = builder.create();

    // Get raw enchantments string.
    String rawEnchantments = CARD_ENCHANTMENTS.getString("cards");
    // Convert raw enchantments string to map of string -> json element.
    ModSaves.HashMapOfJsonElement jsonMap = gson.fromJson(rawEnchantments, new TypeToken<ModSaves.HashMapOfJsonElement>() {
    }.getType());
    for (Map.Entry<String, JsonElement> entry : jsonMap.entrySet()) {
      // For each json element convert to list of card modifier, and save in PERSISTED_ENCHANTMENTS.
      ArrayList<AbstractCardModifier> cardMods = gson.fromJson(entry.getValue(), new TypeToken<ArrayList<AbstractCardModifier>>() {
      }.getType());
      PERSISTED_ENCHANTMENTS.put(entry.getKey(), cardMods);
    }
  }

}
