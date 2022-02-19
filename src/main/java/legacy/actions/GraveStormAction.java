package legacy.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * Action effect from the card GraveStorm. Does most of the heavy lifting, dunno if it's necessary, but I'm copying
 * the design pattern from FiendFire.
 */
public class GraveStormAction extends AbstractGameAction {

  private final DamageInfo info;

  public GraveStormAction(AbstractCreature target, DamageInfo info) {
    this.info = info;
    this.setValues(target, info);
    this.actionType = ActionType.WAIT;
    // TODO: Add a new attack effect.
    this.attackEffect = AttackEffect.FIRE;
    this.duration = Settings.ACTION_DUR_FAST;
  }

  public void update() {
    int count = AbstractDungeon.player.discardPile.size();
    for (int i = 0; i < count; ++i) {
      this.addToTop(new DamageAction(this.target, this.info, this.attackEffect));
    }

    // Exhaust all the graveyard cards! I would rather just iterate through all the cards in the discard and exhaust
    // them, but that runs into a concurrent modification problem. Instead we just use the STSLib MoveCardsAction
    // again and specify ~999 cards.
    this.addToTop(new MoveCardsAction(AbstractDungeon.player.exhaustPile, AbstractDungeon.player.discardPile, 999));
    this.isDone = true;
  }
}
