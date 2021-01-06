package legacy.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

public class GetAllCardsById {

  public static HashSet<AbstractCard> get(String cardId) {
    HashSet<AbstractCard> cards = new HashSet<>();
    if (AbstractDungeon.player.cardInUse.cardID.equals(cardId)) {
      cards.add(AbstractDungeon.player.cardInUse);
    }

    Iterator var2 = AbstractDungeon.player.drawPile.group.iterator();

    AbstractCard c;
    while (var2.hasNext()) {
      c = (AbstractCard) var2.next();
      if (c.cardID.equals(cardId)) {
        cards.add(c);
      }
    }

    var2 = AbstractDungeon.player.discardPile.group.iterator();

    while (var2.hasNext()) {
      c = (AbstractCard) var2.next();
      if (c.cardID.equals(cardId)) {
        cards.add(c);
      }
    }

    var2 = AbstractDungeon.player.exhaustPile.group.iterator();

    while (var2.hasNext()) {
      c = (AbstractCard) var2.next();
      if (c.cardID.equals(cardId)) {
        cards.add(c);
      }
    }

    var2 = AbstractDungeon.player.limbo.group.iterator();

    while (var2.hasNext()) {
      c = (AbstractCard) var2.next();
      if (c.cardID.equals(cardId)) {
        cards.add(c);
      }
    }

    var2 = AbstractDungeon.player.hand.group.iterator();

    while (var2.hasNext()) {
      c = (AbstractCard) var2.next();
      if (c.cardID.equals(cardId)) {
        cards.add(c);
      }
    }

    return cards;
  }
}