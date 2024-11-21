package legacy.util;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.core.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Utility class to get all cards by ID. This searches draw, discard, exhaust, limbo, and deck.
 */
public class CardUtils {

  public static Random RANDOM = new Random();

  public static boolean isCardInstanceInMasterDeck(UUID uuid) {
    if (AbstractDungeon.player == null || AbstractDungeon.player.masterDeck == null) return false;

    for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
      if (card.uuid.equals(uuid)) return true;
    }

    return false;
  }

  public static HashSet<AbstractCard> getAllInstancesOfCardInCombat(String cardId) {
    HashSet<AbstractCard> cards = new HashSet<>();
    if (AbstractDungeon.player.cardInUse.cardID.equals(cardId)) {
      cards.add(AbstractDungeon.player.cardInUse);
    }

    List<Iterator<AbstractCard>> iterators = new ArrayList<Iterator<AbstractCard>>() {{
      add(AbstractDungeon.player.drawPile.group.iterator());
      add(AbstractDungeon.player.discardPile.group.iterator());
      add(AbstractDungeon.player.limbo.group.iterator());
      add(AbstractDungeon.player.exhaustPile.group.iterator());
      add(AbstractDungeon.player.hand.group.iterator());
    }};

    AbstractCard c;

    for (Iterator<AbstractCard> iterator : iterators) {
      while (iterator.hasNext()) {
        c = iterator.next();
        if (c.cardID.equals(cardId)) {
          cards.add(c);
        }
      }
    }

    return cards;
  }

  // I honestly have no clue why isMultiDamage is protected. I shouldn't have to write this reflection hack.
  public static boolean isAOE(AbstractCard card) {
    System.out.println("isMulti: " + ReflectionUtils.<Boolean>getFieldValue(card, "isMultiDamage", false));
    return ReflectionUtils.<Boolean>getFieldValue(card, "isMultiDamage", false);
  }

  // Create a card group from a collection of cards.
  public static CardGroup makeCardGroup(Collection<? extends AbstractCard> cards) {
    CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    cards.forEach(group::addToBottom);
    return group;
  }

  // Reduce the cost of a card by X for the rest of combat.
  public static void reduceCardCost(AbstractCard card, int amount) {
    if (card.costForTurn > 0) {
      card.cost = Math.max(card.cost - amount, 0);
      card.costForTurn = Math.max(card.costForTurn - amount, 0);
      card.isCostModified = true;
      card.superFlash(Color.GOLD.cpy());
    }
  }
}