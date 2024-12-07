package legacy.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

/**
 * Build up charge until we hit 10 stacks, then stun the enemy.
 */
public class LightningPower extends LegacyPower {

  public static final String POWER_ID = "legacy:lightning_power";
  public static int BASE_STUN = 10;

  public LightningPower(AbstractCreature owner, int amount) {
    super(POWER_ID, owner, amount, PowerType.DEBUFF);
  }

  public int getStunStacks() {
    AbstractPower resistance = this.owner.getPower(LightningResistancePower.POWER_ID);
    int resistanceAmount = (resistance == null) ? 0 : resistance.amount;
    return BASE_STUN + resistanceAmount;
  }

  @Override
  public void stackPower(int stackAmount) {
    super.stackPower(stackAmount);

    // If we get to 10 stacks of lightning, stun the enemy and remove ALL stacks.
    if (this.amount < this.getStunStacks()) return;

    this.addToBot(new StunMonsterAction((AbstractMonster) this.owner, this.owner));
    this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    this.addToBot(new ApplyPowerAction(this.owner, this.owner, new LightningResistancePower(this.owner, 5), 5));
  }

  @Override
  public void updateDescription() {
    this.description = this.powerStrings.DESCRIPTIONS[0] + this.getStunStacks() + this.powerStrings.DESCRIPTIONS[1];
  }

  @Override
  public void atStartOfTurn() {
    if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) return;

    this.flashWithoutSound();
    this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
  }
}
