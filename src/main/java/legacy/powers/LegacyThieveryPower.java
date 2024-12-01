package legacy.powers;

import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;

/**
 * Whenever you deal unblocked attack damage, gain gold.
 */
public class LegacyThieveryPower extends LegacyPower {
  public static final String POWER_ID = "legacy:thievery_power";

  public LegacyThieveryPower(AbstractCreature owner, int amount) {
    super(POWER_ID, owner, amount, PowerType.BUFF);
  }

  @Override
  public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
    if (damageAmount <= 0) return;

    this.addToTop(new GainGoldAction(this.amount));
    for (int i = 0; i < this.amount; ++i) {
      AbstractDungeon.effectList.add(new GainPennyEffect(this.owner, this.owner.hb.cX, this.owner.hb.cY, this.owner.hb.cX, this.owner.hb.cY, true));
    }
  }


}
