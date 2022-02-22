package legacy.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import legacy.LegacyMod;
import legacy.cards.equipment.armor.*;
import legacy.cards.equipment.weapons.*;
import legacy.db.DBCardInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base values for weapon & armor cards. We need to load the base values (block / damage) of the cards from the
 * database. This provides an easy way to seed the database with initial base values, without needing to use the
 * card classes directly. (There's a circular dependency if we try to do that).
 */
public class LegacyCards {

  public static Map<String, Class<? extends CustomCard>> allCards;
  public static Map<String, EquipmentType> cardsToType;
  public static Map<String, Integer> cardBaseValues;

  public enum EquipmentType {
    WEAPON,
    ARMOR
  }

  public static void initialize() {
    allCards = new HashMap<>();
    cardsToType = new HashMap<>();
    cardBaseValues = new HashMap<>();

    // Weapons.
    addCard("legacy:anathema", Anathema.class, EquipmentType.WEAPON,15);
    addCard("legacy:dagger", Dagger.class, EquipmentType.WEAPON, 2);
    addCard("legacy:greatsword", Greatsword.class, EquipmentType.WEAPON, 10);
    addCard("legacy:kukri", Kukri.class, EquipmentType.WEAPON, 2);
    addCard("legacy:longsword", Longsword.class, EquipmentType.WEAPON, 6);
    addCard("legacy:mace", Mace.class, EquipmentType.WEAPON, 5);
    addCard("legacy:nunchaku", Nunchaku.class, EquipmentType.WEAPON, 3);
    addCard("legacy:rapier", Rapier.class, EquipmentType.WEAPON, 4);
    addCard("legacy:shortbow", Shortbow.class, EquipmentType.WEAPON, 5);
    addCard("legacy:spear", Spear.class, EquipmentType.WEAPON, 9);
    addCard("legacy:whip", Whip.class, EquipmentType.WEAPON, 2);

    // Armor.
    addCard("legacy:full_plate", FullPlate.class, EquipmentType.ARMOR, 7);
    addCard("legacy:padded_armor", PaddedArmor.class, EquipmentType.ARMOR, 3);
    addCard("legacy:steel_shield", SteelShield.class, EquipmentType.ARMOR, 4);
  }

  public static void addCard(String cardId, Class<? extends CustomCard> c, EquipmentType type, Integer baseValue) {
    allCards.put(cardId, c);
    cardsToType.put(cardId, type);
    cardBaseValues.put(cardId, baseValue);
  }

  public static List<DBCardInfo> getAllCardsDbInfo() {
    List<DBCardInfo> cards = new ArrayList<>();
    for (Map.Entry<String, Integer> entry : cardBaseValues.entrySet()) {
      cards.add(new DBCardInfo(entry.getKey(), entry.getValue()));
    }

    return cards;
  }

  /**
   * Gets instances of all cards. Used to initialize all cards in LegacyMod.
   */
  public static List<AbstractCard> getAllCards(){
    List<AbstractCard> cards = new ArrayList<>();
    for (Class<? extends CustomCard> c : allCards.values()) {
      try {
        cards.add(c.newInstance());
      } catch (Exception e) {
        System.out.println(e);
      }
    }

    return cards;
  }

  /**
   * Gets an instance of a card class by cardId. This creates a new instance of Class(),
   * and does not find a particular instance via UUID. For that, use CardUtils.
   */
  public static AbstractCard getInstanceById(String cardId) {
    try {
      Class<? extends CustomCard> c = allCards.get(cardId);
      return c.newInstance();
    } catch (Exception e) {
      System.out.println(e);
    }

    return null;
  }

  public static String getImagePath(String type, String id) {
    return LegacyMod.MOD_ID + "/images/cards/" + type.toLowerCase() + "/" + LegacyMod.getNameFromId(id) + ".png";
  }

}
