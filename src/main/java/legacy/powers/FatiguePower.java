package legacy.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * The inverse of Energized, lose energy next turn.
 */
public class FatiguePower extends LegacyPower {

  public static final String POWER_ID = "legacy:fatigue_power";

  public FatiguePower(AbstractCreature owner, AbstractCreature source, int amount) {
    super(POWER_ID, owner, amount, PowerType.DEBUFF);
  }

  @Override
  public void onEnergyRecharge() {
    this.flash();
    AbstractDungeon.player.gainEnergy(this.amount);
    this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, "legacy:fatigue_power"));
  }
}
