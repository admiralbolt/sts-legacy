package legacy.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ThornsPower;

/**
 * Lose thorns at end of turn.
 */
public class LoseThornsPower extends LegacyPower {

  public static final String POWER_ID = "legacy:lose_thorns";

  public LoseThornsPower(AbstractCreature owner, int amount) {
    super(POWER_ID, owner, amount, PowerType.DEBUFF);
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    this.flash();
    this.addToBot(new ApplyPowerAction(this.owner, this.owner, new ThornsPower(this.owner, -this.amount), -this.amount));
    this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
  }

}
