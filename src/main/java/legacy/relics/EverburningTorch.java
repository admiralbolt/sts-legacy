package legacy.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;

public class EverburningTorch extends LegacyRelic {

  public static final String RELIC_ID = "legacy:everburning_torch";

  public EverburningTorch() {
    super(RELIC_ID, RelicTier.STARTER, LandingSound.MAGICAL);
  }

  @Override
  public void atBattleStart() {
    this.flash();
    this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    this.addToBot(new ScryAction(this.counter));
    this.addToBot(new DrawCardAction(AbstractDungeon.player, 1));
  }

  @Override
  public AbstractRelic makeCopy() {
    return new EverburningTorch();
  }

  @Override
  public void onVictory() {
    if (!(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss)) return;

    this.incrementCharge();
    this.description = this.getUpdatedDescription();
    this.initializeTips();
  }


}