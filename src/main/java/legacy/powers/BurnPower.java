package legacy.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

/**
 * Burn for damage! Doesn't get reduced at end of turn, will fade completely if the target doesn't have burn
 * applied to it again within 3 turns.
 */
public class BurnPower extends LegacyTwoAmountPower implements HealthBarRenderPower {

  public static final String POWER_ID = "legacy:burn_power";
  public static final Color HEALTH_BAR_COLOR = Color.valueOf("#cc5500");
  public static final int BURN_TURNS = 3;

  public BurnPower(AbstractCreature owner, int amount) {
    super(POWER_ID, owner, BURN_TURNS, amount, PowerType.DEBUFF);
  }

  @Override
  public void playApplyPowerSfx() {
    CardCrawlGame.sound.play("ATTACK_FIRE", 0.05f);
  }

  @Override
  public void atStartOfTurn() {
    if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) return;

    this.flashWithoutSound();
    this.addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, this.amount2, DamageInfo.DamageType.HP_LOSS)));
    this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
  }

  @Override
  public void stackPower(int stackAmount) {
    super.stackPower(stackAmount);

    // Refresh burn duration when we re-apply.
    this.amount = BURN_TURNS;
  }

  @Override
  public int getHealthBarAmount() {
    return this.amount;
  }

  @Override
  public Color getColor() {
    return HEALTH_BAR_COLOR;
  }

  @Override
  public void updateDescription() {
    this.description =
            // Choose between player text & monster text.
            this.powerStrings.DESCRIPTIONS[(this.owner != null && this.owner.isPlayer) ? 0 : 1] +
            this.amount2 + this.powerStrings.DESCRIPTIONS[2] +
            // Choose between "turns" and "turn".
            this.powerStrings.DESCRIPTIONS[(this.amount > 1) ? 3 : 4];
  }
}
