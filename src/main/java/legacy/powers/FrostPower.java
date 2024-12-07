package legacy.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * Based off the runesmith's IceCold power.
 * Credit where it's due: https://github.com/PureStream/Runesmith.
 *
 * Reduces damage equal to stacks.
 */
public class FrostPower extends LegacyPower {

  public static final String POWER_ID = "legacy:frost_power";

  public FrostPower(AbstractCreature owner, int amount) {
    super(POWER_ID, owner, amount, PowerType.DEBUFF);
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    flash();
    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
  }

  @Override
  public float atDamageGive(float damage, DamageInfo.DamageType type) {
    return type == DamageInfo.DamageType.NORMAL ? damage - (float)this.amount : damage;
  }
}
