package legacy.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import legacy.util.CardUtils;

public class MagnifyingGlassAction extends AbstractGameAction {

  public static final String ID = "legacy:magnifying_glass_action";
  public static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ID);

  public MagnifyingGlassAction(int amount) {
    this.amount = amount;
    this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
  }


  @Override
  public void update() {
    if (this.duration == this.startDuration) {
      if (AbstractDungeon.player.hand.isEmpty()) {
        this.isDone = true;
        return;
      }

      if (AbstractDungeon.player.hand.size() <= this.amount) {
        this.amount = AbstractDungeon.player.hand.size();
        for (int i = 0; i < this.amount; ++i) {
          CardUtils.reduceCardCost(AbstractDungeon.player.hand.getTopCard(), 1);
        }
        return;
      }

      AbstractDungeon.handCardSelectScreen.open(UI_STRINGS.TEXT[0], this.amount, true, true);
      this.tickDuration();
      return;
    }

    if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
      for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
        CardUtils.reduceCardCost(card, 1);
        AbstractDungeon.player.hand.addToHand(card);
      }
      AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
    }

    this.tickDuration();
  }
}