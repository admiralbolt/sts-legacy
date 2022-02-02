package legacy.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import legacy.cards.equipment.armor.PaddedArmor;
import legacy.cards.equipment.armor.SteelShield;
import legacy.cards.equipment.weapons.*;
import legacy.db.DBCardInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegacyCards {

  public static Map<String, Class<? extends CustomCard>> allCards;
  public static Map<String, LegacyCardType> cardsByType;
  public static Map<String, Integer> cardBaseValues;

  public enum LegacyCardType {
    WEAPON,
    ARMOR,
    PRESTIGE_CLASSES
  }

  public static void initialize() {
    allCards = new HashMap<>();
    cardsByType = new HashMap<>();
    cardBaseValues = new HashMap<>();

    // Weapons.
    addCard("legacy:anathema", Anathema.class, LegacyCardType.WEAPON,15);
    addCard("legacy:dagger", Dagger.class, LegacyCardType.WEAPON, 2);
    addCard("legacy:greatsword", Greatsword.class, LegacyCardType.WEAPON, 10);
    addCard("legacy:kukri", Kukri.class, LegacyCardType.WEAPON, 2);
    addCard("legacy:longsword", Longsword.class, LegacyCardType.WEAPON, 6);
    addCard("legacy:mace", Mace.class, LegacyCardType.WEAPON, 5);
    addCard("legacy:rapier", Rapier.class, LegacyCardType.WEAPON, 4);
    addCard("legacy:spear", Spear.class, LegacyCardType.WEAPON, 9);
    addCard("legacy:whip", Whip.class, LegacyCardType.WEAPON, 2);

    // Armor.
    addCard("legacy:padded_armor", PaddedArmor.class, LegacyCardType.ARMOR, 3);
    addCard("legacy:steel_shield", SteelShield.class, LegacyCardType.ARMOR, 4);
  }

  public static void addCard(String cardId, Class<? extends CustomCard> c, LegacyCardType type, Integer baseValue) {
    allCards.put(cardId, c);
    cardsByType.put(cardId, type);
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

}
