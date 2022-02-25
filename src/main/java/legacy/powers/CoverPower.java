package legacy.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

/**
 * Reduce ALL damage taken by 50%.
 */
public class CoverPower extends LegacyPower {

  public static String POWER_ID = "legacy:cover_power";

  public CoverPower(AbstractCreature owner, int amount) {
    super(POWER_ID, owner, amount, PowerType.BUFF);
  }

  @Override
  public float atDamageReceive(float damage, final DamageInfo.DamageType type) {
    return (damage > 1.0f) ? damage * 0.5f : damage;
  }

  @Override
  public float atDamageGive(float damage, DamageInfo.DamageType type) {
    return (damage > 1.0f) ? damage * 0.5f : damage;
  }

  @Override
  public void atEndOfRound() {
    this.flash();
    this.addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
  }

  @Override
  public void updateDescription() {
    this.description = this.powerStrings.DESCRIPTIONS[0];
  }
}
