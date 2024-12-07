package legacy.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

/**
 * Build up charge until we hit 10 stacks, then stun the enemy.
 */
public class LightningPower extends LegacyPower {

  public static final String POWER_ID = "legacy:lightning_power";

  public LightningPower(AbstractCreature owner, int amount) {
    super(POWER_ID, owner, amount, PowerType.DEBUFF);
  }

  @Override
  public void stackPower(int stackAmount) {
    super.stackPower(stackAmount);

    // If we get to 10 stacks of lightning, stun the enemy and remove ALL stacks.
    if (this.amount < 10) return;

    this.addToBot(new StunMonsterAction((AbstractMonster) this.owner, this.owner));
    this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
  }

  @Override
  public void updateDescription() {
    this.description = this.powerStrings.DESCRIPTIONS[0];
  }

  @Override
  public void atStartOfTurn() {
    if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) return;

    this.flashWithoutSound();
    this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
  }
}
