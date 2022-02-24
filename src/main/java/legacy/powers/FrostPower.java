package legacy.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.GiantHead;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.SlowPower;

/**
 * Currently, this is a straight up rip off from the runesmith's IceCold power.
 * Credit where it's due: https://github.com/PureStream/Runesmith.
 *
 * Reduces damage equal to stacks. The target also receives the Slow debuff while frost is applied.
 */
public class FrostPower extends LegacyPower {

  public static final String POWER_ID = "legacy:frost";

  public FrostPower(AbstractCreature owner, int amount) {
    super(POWER_ID, owner, amount, PowerType.DEBUFF);
  }

  @Override
  public void onInitialApplication() {
    super.onInitialApplication();
    applySlowIf5Stacks();
  }

  @Override
  public void stackPower(int stackAmount) {
    super.stackPower(stackAmount);
    applySlowIf5Stacks();
  }

  @Override
  public void reducePower(int reduceAmount) {
    super.reducePower(reduceAmount);

    if (this.amount < 5) {
      removeSlow();
    }
  }

  private void applySlowIf5Stacks() {
    if (this.amount < 5) return;

    // We don't want to apply slow if the target already has it.
    AbstractPower slow = this.owner.getPower("Slow");
    if (slow == null) AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new SlowPower(this.owner, 0)));
  }

  private void removeSlow() {
    // Giant Head shouldn't be unslowed from getting thawed. :)
    if (this.owner instanceof GiantHead) return;

    AbstractPower slow = this.owner.getPower("Slow");
    if (slow != null) AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, slow));
  }

  // Remove the slow power after thawing.
  @Override
  public void onRemove() {
    super.onRemove();

    this.removeSlow();
  }

  public void atEndOfTurn(boolean isPlayer) {
    flash();
    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, "legacy:frost", 1));
  }

  public float atDamageGive(float damage, DamageInfo.DamageType type) {
    return type == DamageInfo.DamageType.NORMAL ? damage - (float)this.amount : damage;
  }

  @Override
  public void updateDescription() {
    // On the last turn it should say "turn" instead of "turns". Hence the weird ternary operator accessing
    // descriptions.
    this.description = this.powerStrings.DESCRIPTIONS[0] + this.amount + this.powerStrings.DESCRIPTIONS[1] + this.amount + this.powerStrings.DESCRIPTIONS[(this.amount == 1) ? 3 : 2];
  }

}
