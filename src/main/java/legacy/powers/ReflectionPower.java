package legacy.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * If you fully block, deal damage back to the attacker.
 *
 * 100% stolen from ReplayTheSpire.
 */
public class ReflectionPower extends LegacyPower {

  public static final String POWER_ID = "legacy:reflection_power";

  public ReflectionPower(AbstractCreature owner, int amount) {
    super(POWER_ID, owner, amount, PowerType.BUFF);
  }

  @Override
  public void updateDescription() {
    this.description = this.powerStrings.DESCRIPTIONS[0];
  }

  @Override
  public void atStartOfTurn() {
    super.atStartOfTurn();

    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
  }

  @Override
  public int onAttacked(DamageInfo info, int damageAmount) {
    if (info.owner != null && info.owner != this.owner && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount <= 0 && info.output > 0) {
      this.flash();
      AbstractDungeon.actionManager.addToTop(new DamageAction(info.owner, new DamageInfo(this.owner, info.output, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
    }
    return damageAmount;
  }
}
