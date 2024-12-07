package legacy.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FocusPower;

/**
 * Lose focus at end of turn.
 */
public class LoseFocusPower extends LegacyPower {

  public static final String POWER_ID = "legacy:lose_focus_power";

  public LoseFocusPower(AbstractCreature owner, int amount) {
    super(POWER_ID, owner, amount, PowerType.DEBUFF);
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    this.flash();
    this.addToBot(new ApplyPowerAction(this.owner, this.owner, new FocusPower(this.owner, -this.amount), -this.amount));
    this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
  }

}
