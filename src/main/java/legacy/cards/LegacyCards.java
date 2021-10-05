package legacy.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import legacy.cards.weapons.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegacyCards {

  public static Map<String, Class<? extends LegacyWeapon>> weapons;

  public static void initialize() {
    weapons = new HashMap<>();
    weapons.put("legacy:anathema", Anathema.class);
    weapons.put("legacy:dagger", Dagger.class);
    weapons.put("legacy:longsword", Longsword.class);
    weapons.put("legacy:mace", Mace.class);
    weapons.put("legacy:rapier", Rapier.class);
    weapons.put("legacy:whip", Whip.class);
  }

  public static List<AbstractCard> getAllCards(){
    List<AbstractCard> cards = new ArrayList<>();
    for (Class<? extends LegacyWeapon> c : weapons.values()) {
      try {
        cards.add(c.newInstance());
      } catch (Exception e) {
        System.out.println(e);
      }
    }
    return cards;
  }

  public static AbstractCard getInstanceById(String cardId) {
    try {
      Class<? extends LegacyWeapon> c = weapons.get(cardId);
      return c.newInstance();
    } catch (Exception e) {
      System.out.println(e);
    }

    return null;
  }

}
