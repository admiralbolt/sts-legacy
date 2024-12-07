package legacy.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Donu;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class ScytheDamageAction extends AbstractGameAction {

  private final int refund;
  private final DamageInfo info;
  private static final float DURATION = 0.1F;

  public ScytheDamageAction(AbstractCreature target, DamageInfo info, int refund) {
    this.info = info;
    this.setValues(target, info);
    this.refund = refund;
    this.actionType = ActionType.DAMAGE;
    this.duration = 0.1F;
  }

  public void update() {
    if (this.duration == 0.1F && this.target != null) {
      AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.SLASH_DIAGONAL));
      this.target.damage(this.info);
      if ((((AbstractMonster)this.target).isDying || this.target.currentHealth <= 0) && !this.target.halfDead) {
        AbstractDungeon.player.gainEnergy(this.refund);
      }

      if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
        AbstractDungeon.actionManager.clearPostCombatActions();
      }
    }

    this.tickDuration();
  }
}
