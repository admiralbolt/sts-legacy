package legacy.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.StrengthPower;

/**
 * Multiply strength by any amount!
 *
 * Interestingly, the default LimitBreakAction refers directly to AbstractDungeon.player and doesn't take a target.
 * This version can be applied to any target.
 */
public class MultiplyStrengthAction extends AbstractGameAction {

  private final AbstractCreature target;
  private final int multiplier;

  public MultiplyStrengthAction(AbstractCreature target, int amount) {
    this.target = target;
    this.multiplier = amount - 1;
  }

  @Override
  public void update() {
    if (this.duration == Settings.ACTION_DUR_XFAST && this.target.hasPower("Strength")) {
      int strengthAdd = this.target.getPower("Strength").amount * this.multiplier;
      addToTop(new ApplyPowerAction(this.target, this.target, new StrengthPower(this.target, strengthAdd), strengthAdd));
    }

    tickDuration();
  }
}
