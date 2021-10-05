package legacy.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import legacy.LegacyMod;
import legacy.util.CardUtils;

/**
 * Permanently increase the base damage of a card.
 *
 * This is done by doing two things:
 * 1. Modifying all base damage instances of cards with the same id. This will modify the damage for a card within
 *    the context of the current loaded game.
 * 2. Queue a change to update the DB. This will save the current damage value which gets loaded during the
 */
public class PermanentBuffAction extends AbstractGameAction {

  private final String cardId;
  private final int increase;

  public PermanentBuffAction(String cardId, int increase) {
    this.cardId = cardId;
    this.increase = increase;
  }

  public void update() {
    int newBaseDamage = -1;
    for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
      if (!card.cardID.equals(cardId)) continue;

      card.baseDamage += this.increase;
      newBaseDamage = card.baseDamage;
    }

    for (AbstractCard card : CardUtils.getAllInstancesOfCard(this.cardId)) {
      card.baseDamage += this.increase;
      newBaseDamage = card.baseDamage;
    }

    for (AbstractCard card : AbstractDungeon.player.hand.group) {
      if (card.cardID.equals(this.cardId)) {
        card.applyPowers();
      }
    }

    LegacyMod.LEGACY_DB.queueChange(this.cardId, newBaseDamage);
    this.isDone = true;
  }
}
