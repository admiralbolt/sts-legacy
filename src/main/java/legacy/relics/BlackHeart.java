package legacy.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

/**
 * On pickup, gain max hp equal to charges. Whenever you defat an elite or boss, gain a charge and 1 max hp.
 */
public class BlackHeart extends LegacyRelic {

  public static final String RELIC_ID = "legacy:black_heart";

  public BlackHeart() {
    super(RELIC_ID, RelicTier.RARE, LandingSound.MAGICAL);
  }

  @Override
  public AbstractRelic makeCopy() {
    return new BlackHeart();
  }

  @Override
  public void onVictory() {
    if (!(AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) && !(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss)) return;

    AbstractDungeon.player.increaseMaxHp(1, true);
    this.incrementCharge();
    this.description = this.getUpdatedDescription();
  }

  @Override
  public void onEquip() {
    AbstractDungeon.player.increaseMaxHp(this.counter, true);
  }

}