package legacy.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import legacy.LegacyMod;
import legacy.util.GetAllCardsById;

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

    for (AbstractCard card : GetAllCardsById.get(this.cardId)) {
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
