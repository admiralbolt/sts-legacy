package legacy.relics;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

/**
 * On pickup, gold equal to charge. Whenever you climb a floor, gain 5 charge.
 */
public class TimelessVault extends LegacyRelic {

  private static final int STARTING_CHARGE = 25;
  public static final String RELIC_ID = "legacy:timeless_vault";

  public TimelessVault() {
    super(RELIC_ID, RelicTier.RARE, LandingSound.MAGICAL);

    // A funny hack here, we want it to start at higher than 1 charge, so we increment
    // the charge value on construction if it's less than our default.
    if (this.counter < STARTING_CHARGE) {
      this.incrementCharge(STARTING_CHARGE - this.counter);
    }
  }

  @Override
  public AbstractRelic makeCopy() {
    return new TimelessVault();
  }

  @Override
  public void onEnterRoom(AbstractRoom room) {
    this.incrementCharge(5);
  }

  @Override
  public void onEquip() {
    CardCrawlGame.sound.play("GOLD_GAIN");
    AbstractDungeon.player.gainGold(this.counter);
  }

}