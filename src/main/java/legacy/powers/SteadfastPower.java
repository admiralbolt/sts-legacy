package legacy.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

/**
 * Vigor, but for block!
 */
public class SteadfastPower extends LegacyPower {

  public static final String POWER_ID = "legacy:steadfast_power";

  public SteadfastPower(AbstractCreature owner, int amount) {
    super(POWER_ID, owner, amount, PowerType.BUFF);
  }

  @Override
  public float modifyBlock(float blockAmount) {
    return blockAmount + this.amount;
  }

  @Override
  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (card.baseBlock <= 0) return;

    this.flash();
    this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
  }

}
