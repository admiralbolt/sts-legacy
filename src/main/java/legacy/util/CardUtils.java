package legacy.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

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
}