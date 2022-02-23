package legacy.cards;

import basemod.abstracts.CustomCard;
import legacy.LegacyMod;
import legacy.cards.mods.enchantments.Enchantment;
import legacy.cards.mods.enchantments.EnchantmentUtils;
import legacy.characters.TheAdventurer;

/**
 * Base class for all my new cards.
 */
public abstract class LegacyCard extends CustomCard {

  // Second magic number used for some stuff.
  public int magicNumberTwo;
  public int baseMagicNumberTwo;
  public boolean upgradedMagicNumberTwo;
  public boolean isMagicNumberTwoModified;

  public LegacyCardType legacyCardType;
  public boolean enchantable = false;

  public enum LegacyCardType {
    WEAPON,
    ARMOR,
    GEAR,
    SPELL,
    CLASS
  }

  public LegacyCard(String id, String name, int cost, String rawDescription, LegacyCardType legacyCardType, CardType type, CardRarity rarity, CardTarget target) {
    super(id, name, getImagePath(legacyCardType, id), cost, rawDescription, type, TheAdventurer.Enums.COLOR_GRAY, rarity, target);

    this.legacyCardType = legacyCardType;
  }

  public LegacyCard(String id, String name, String image, int cost, String rawDescription, CardType type, CardRarity rarity, CardTarget target) {
    super(id, name, image, cost, rawDescription, type, TheAdventurer.Enums.COLOR_GRAY, rarity, target);
  }

  protected void upgradeMagicNumberTwo(int amount) {
    this.baseMagicNumberTwo += amount;
    this.magicNumberTwo = this.baseMagicNumberTwo;
    this.upgradedMagicNumberTwo = true;
  }

  // Updates the card name based on current enchantments.
  public void updateName() {
    StringBuilder builder = new StringBuilder();
    for (Enchantment enchantment : EnchantmentUtils.getCardEnchantments(this)) {
      builder.append(enchantment.name);
      builder.append(" ");
    }

    builder.append(this.originalName);
    this.name = builder.toString();
    this.initializeTitle();
  }

  public static String getImagePath(LegacyCardType type, String id) {
    return LegacyMod.MOD_ID + "/images/cards/" + type.toString().toLowerCase() + "/" + LegacyMod.getNameFromId(id) + ".png";
  }

}
