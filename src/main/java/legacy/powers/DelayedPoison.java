package legacy.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

/**
 * Re-apply poison to an enemy at the end of the monsters turn.
 */
public class DelayedPoison extends LegacyPower {

  public static final String POWER_ID = "legacy:delayed_poison";
  private final AbstractCreature source;

  public DelayedPoison(AbstractCreature owner, AbstractCreature source, int amount) {
    super(POWER_ID, owner, amount, PowerType.DEBUFF);

    this.source = source;
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    if (this.owner.isDead || this.owner.isDying) return;

    this.flash();
    this.addToBot(new ApplyPowerAction(this.owner, this.source, new PoisonPower(this.owner, this.source, this.amount), this.amount));
    this.addToBot(new RemoveSpecificPowerAction(this.owner, this.source, POWER_ID));
  }

  @Override
  public void stackPower(int stackAmount) {
    this.fontScale = 8.0F;
    this.amount += stackAmount;
  }

}
