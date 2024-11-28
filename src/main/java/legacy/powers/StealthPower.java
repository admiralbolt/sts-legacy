package legacy.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * At the end of turn, gain block equal to dexterity.
 */
public class StealthPower extends LegacyPower {
  public static final String POWER_ID = "legacy:stealth_power";

  public StealthPower(AbstractCreature owner, int amount) {
    super(POWER_ID, owner, amount, PowerType.BUFF);
  }

  @Override
  public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
    this.flash();
    AbstractPower dexterity = this.owner.getPower("Dexterity");
    int dexterityAmount = (dexterity == null) ? 0 : dexterity.amount;
    System.out.println("isPlayer: " + isPlayer + ", owner: " + this.owner + ", dexterity: " + dexterityAmount);
    if (dexterityAmount == 0) return;

    this.addToBot(new GainBlockAction(this.owner, this.owner, dexterityAmount));
  }

  @Override
  public void updateDescription() {
    this.description = this.powerStrings.DESCRIPTIONS[0];
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    flash();
    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
  }

}
