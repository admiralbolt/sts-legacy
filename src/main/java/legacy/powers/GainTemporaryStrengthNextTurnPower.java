package legacy.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

/**
 * It's in the class name. Gain temporary strength next turn.
 */
public class GainTemporaryStrengthNextTurnPower extends LegacyPower {

  public static final String POWER_ID = "legacy:next_turn_tmp_strength_power";

  public GainTemporaryStrengthNextTurnPower(AbstractCreature owner, int amount) {
    super(POWER_ID, owner, amount, PowerType.BUFF);
  }

  public void atStartOfTurnPostDraw() {
    this.flash();
    this.addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
    this.addToBot(new ApplyPowerAction(this.owner, this.owner, new LoseStrengthPower(this.owner, this.amount), this.amount));
    this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, GainTemporaryStrengthNextTurnPower.POWER_ID));
  }

}
