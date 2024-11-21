package legacy;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import legacy.base_classes.LevelUpChoiceCard;
import legacy.cards.mods.enchantments.EnchantmentChoice;
import legacy.cards.mods.enchantments.EnchantmentUtils;
import legacy.cards.vars.MagicNumberTwoVariable;
import legacy.characters.TheAdventurer;
import legacy.events.EnchantmentShrine;
import legacy.potions.PlaceholderPotion;
import legacy.relics.BottledPlaceholderRelic;
import legacy.relics.DefaultClickableRelic;
import legacy.relics.PlaceholderRelic;
import legacy.relics.PlaceholderRelic2;
import legacy.ui.TopPanelXPItem;
import legacy.util.MonsterUtils;
import legacy.util.TextureLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;


@SpireInitializer
public class LegacyMod implements
  EditCardsSubscriber,
  EditRelicsSubscriber,
  EditStringsSubscriber,
  EditKeywordsSubscriber,
  EditCharactersSubscriber,
  PostInitializeSubscriber {
  public static final String MOD_ID = "legacy";

  //This is for the in-game mod settings panel.
  private static final String MODNAME = "Legacy";
  private static final String AUTHOR = "Admiral Lightning Bolt";
  private static final String DESCRIPTION = "Slay the Spire with no perma-death. Sort of.";

  // Character stats! This is experience points and levels.
  public static SpireConfig CHARACTER_STATS;
  // Card enchantments! These are all enchantments applied to a card.
  public static SpireConfig CARD_ENCHANTMENTS;

  // =============== INPUT TEXTURE LOCATION =================

  // Colors (RGB)
  // Character Color
  public static final Color DEFAULT_GRAY = CardHelper.getColor(64.0f, 70.0f, 70.0f);

  // Potion Colors in RGB
  public static final Color PLACEHOLDER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f); // Orange-ish Red
  public static final Color PLACEHOLDER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f); // Near White
  public static final Color PLACEHOLDER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f); // Super Dark Red/Brown

  // Card backgrounds - The actual rectangular card.
  private static final String ATTACK_DEFAULT_GRAY = "legacy/images/512/bg_attack_default_gray.png";
  private static final String SKILL_DEFAULT_GRAY = "legacy/images/512/bg_skill_default_gray.png";
  private static final String POWER_DEFAULT_GRAY = "legacy/images/512/bg_power_default_gray.png";

  private static final String ENERGY_ORB_DEFAULT_GRAY = "legacy/images/512/card_default_gray_orb.png";
  private static final String CARD_ENERGY_ORB = "legacy/images/512/card_small_orb.png";

  private static final String ATTACK_DEFAULT_GRAY_PORTRAIT = "legacy/images/1024/bg_attack_default_gray.png";
  private static final String SKILL_DEFAULT_GRAY_PORTRAIT = "legacy/images/1024/bg_skill_default_gray.png";
  private static final String POWER_DEFAULT_GRAY_PORTRAIT = "legacy/images/1024/bg_power_default_gray.png";
  private static final String ENERGY_ORB_DEFAULT_GRAY_PORTRAIT = "legacy/images/1024/card_default_gray_orb.png";

  // Character assets
  private static final String THE_DEFAULT_BUTTON = "legacy/images/charSelect/DefaultCharacterButton.png";
  private static final String THE_DEFAULT_PORTRAIT = "legacy/images/charSelect/DefaultCharacterPortraitBG.png";
  public static final String THE_DEFAULT_SHOULDER_1 = "legacy/images/char/defaultCharacter/shoulder.png";
  public static final String THE_DEFAULT_SHOULDER_2 = "legacy/images/char/defaultCharacter/shoulder2.png";
  public static final String THE_DEFAULT_CORPSE = "legacy/images/char/defaultCharacter/corpse.png";

  //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
  public static final String BADGE_IMAGE = "legacy/images/Badge.png";

  // Atlas and JSON files for the Animations
  public static final String THE_DEFAULT_SKELETON_ATLAS = "legacy/images/char/defaultCharacter/skeleton.atlas";
  public static final String THE_DEFAULT_SKELETON_JSON = "legacy/images/char/defaultCharacter/skeleton.json";

  public static String getNameFromId(String id) {
    String[] bits = id.split(":");
    return bits[1];
  }

  public static String makeRelicPath(String resourcePath) {
    return MOD_ID + "/images/relics/" + resourcePath;
  }

  public static String makeRelicOutlinePath(String resourcePath) {
    return MOD_ID + "/images/relics/outline/" + resourcePath;
  }

  private static void loadCharacterStats() {
    Properties defaults = new Properties();
    defaults.setProperty("xp", "0");
    defaults.setProperty("next_level_xp", "10");
    defaults.setProperty("level", "0");
    defaults.setProperty("fighter_level", "0");
    defaults.setProperty("rogue_level", "0");
    defaults.setProperty("wizard_level", "0");

    try {
      CHARACTER_STATS = new SpireConfig("Legacy", "character_stats", defaults);
      CHARACTER_STATS.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void loadCardEnchantments() {
    Properties defaults = new Properties();
    defaults.setProperty("cards", "{}");
    try {
      CARD_ENCHANTMENTS = new SpireConfig("Legacy", "card_enchantments", defaults);
      CARD_ENCHANTMENTS.load();
      EnchantmentUtils.loadEnchantments();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public LegacyMod() {
    BaseMod.subscribe(this);

    BaseMod.addColor(TheAdventurer.Enums.COLOR_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
      DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
      ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY, ENERGY_ORB_DEFAULT_GRAY,
      ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
      ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB);
  }

  // Save permanent stats.
  public static void saveCharacterStats() {
    try {
      if (AbstractDungeon.player instanceof TheAdventurer) {
        ((TheAdventurer) AbstractDungeon.player).commitStats();
      }
      CHARACTER_STATS.save();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Save card enchantments.
  public static void saveCardEnchantments() {
    try {
      EnchantmentUtils.commitEnchantments();
      CARD_ENCHANTMENTS.save();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void initialize() {
    LegacyMod legacymod = new LegacyMod();
  }

  @Override
  public void receiveEditCharacters() {
    BaseMod.addCharacter(new TheAdventurer("the Default", TheAdventurer.Enums.THE_DEFAULT),
      THE_DEFAULT_BUTTON, THE_DEFAULT_PORTRAIT, TheAdventurer.Enums.THE_DEFAULT);

    receiveEditPotions();
  }

  // =============== /LOAD THE CHARACTER/ =================


  // =============== POST-INITIALIZE =================

  @Override
  public void receivePostInitialize() {
    // Load the Mod Badge
    Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

    // Create the Mod Menu
    ModPanel settingsPanel = new ModPanel();

    BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

    MonsterUtils.postInitialize();

    EnchantmentUtils.postInitialize();

    // Brand new display for XP!
    BaseMod.addTopPanelItem(new TopPanelXPItem());

    // =============== EVENTS =================
    BaseMod.addEvent(EnchantmentShrine.ID, EnchantmentShrine.class);
  }

  public void receiveEditPotions() {
    // Class Specific Potion. If you want your potion to not be class-specific,
    // just remove the player class at the end (in this case the "TheDefaultEnum.THE_DEFAULT".
    // Remember, you can press ctrl+P inside parentheses like addPotions)
    BaseMod.addPotion(PlaceholderPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, PlaceholderPotion.POTION_ID, TheAdventurer.Enums.THE_DEFAULT);
  }

  @Override
  public void receiveEditRelics() {
    // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
    BaseMod.addRelicToCustomPool(new PlaceholderRelic(), TheAdventurer.Enums.COLOR_GRAY);
    BaseMod.addRelicToCustomPool(new BottledPlaceholderRelic(), TheAdventurer.Enums.COLOR_GRAY);
    BaseMod.addRelicToCustomPool(new DefaultClickableRelic(), TheAdventurer.Enums.COLOR_GRAY);

    // This adds a relic to the Shared pool. Every character can find this relic.
    BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);

    // Mark relics as seen (the others are all starters so they're marked as seen in the character file
    UnlockTracker.markRelicAsSeen(BottledPlaceholderRelic.ID);
  }

  @Override
  public void receiveEditCards() {
    loadCharacterStats();
    loadCardEnchantments();
    EnchantmentUtils.initialize();

    new AutoAdd(LegacyMod.MOD_ID)
      .notPackageFilter(EnchantmentChoice.class)
      .notPackageFilter(LevelUpChoiceCard.class)
      .setDefaultSeen(true)
      .cards();

    // Add card vars.
    BaseMod.addDynamicVariable(new MagicNumberTwoVariable());
  }

  @Override
  public void receiveEditStrings() {
    // CardStrings
    BaseMod.loadCustomStringsFile(CardStrings.class,
      MOD_ID + "/localization/eng/Legacy-Card-Strings.json");

    // PowerStrings
    BaseMod.loadCustomStringsFile(PowerStrings.class,
      MOD_ID + "/localization/eng/Legacy-Power-Strings.json");

    // RelicStrings
    BaseMod.loadCustomStringsFile(RelicStrings.class,
      MOD_ID + "/localization/eng/Legacy-Relic-Strings.json");

    // Event Strings
    BaseMod.loadCustomStringsFile(EventStrings.class,
      MOD_ID + "/localization/eng/Legacy-Event-Strings.json");

    // PotionStrings
    BaseMod.loadCustomStringsFile(PotionStrings.class,
      MOD_ID + "/localization/eng/Legacy-Potion-Strings.json");

    // CharacterStrings
    BaseMod.loadCustomStringsFile(CharacterStrings.class,
      MOD_ID + "/localization/eng/Legacy-Character-Strings.json");

    // OrbStrings
    BaseMod.loadCustomStringsFile(OrbStrings.class,
      MOD_ID + "/localization/eng/Legacy-Orb-Strings.json");

    // UIStrings.
    BaseMod.loadCustomStringsFile(UIStrings.class,
      MOD_ID + "/localization/eng/Legacy-UI-Strings.json");

    // BlightStrings.
    BaseMod.loadCustomStringsFile(BlightStrings.class, MOD_ID + "/localization/eng/Legacy-Blight-Strings.json");
  }

  @Override
  public void receiveEditKeywords() {
    // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
    //
    // Multiword keywords on cards are done With_Underscores
    //
    // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
    // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
    // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword

    Gson gson = new Gson();
    String json = Gdx.files.internal(MOD_ID + "/localization/eng/Legacy-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
    com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

    if (keywords == null) return;
    for (Keyword keyword : keywords) {
      BaseMod.addKeyword(MOD_ID.toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
    }
  }


  // ================ /LOAD THE KEYWORDS/ ===================  

  // this adds "ModName:" before the ID of any card/relic/power etc.
  // in order to avoid conflicts if any other mod uses the same ID.
  public static String makeID(String idText) {
    return MOD_ID + ":" + idText;
  }
}
