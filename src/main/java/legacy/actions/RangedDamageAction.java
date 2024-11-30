package legacy.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * A clone of DamageAction that ignores block & thorns.
 */
public class RangedDamageAction extends AbstractGameAction {

  public static final String[] IGNORED_POWERS = {
    CurlUpPower.POWER_ID, FlameBarrierPower.POWER_ID,
    MalleablePower.POWER_ID, ThornsPower.POWER_ID,
  };

  private final DamageInfo info;
  private final boolean ignoreBlock;

  public RangedDamageAction(AbstractCreature target, AbstractCreature source, int damage) {
    this(target, source, damage, AttackEffect.SLASH_HORIZONTAL, true);
  }

  public RangedDamageAction(AbstractCreature target, AbstractCreature source, int damage, AttackEffect attackEffect, boolean ignoreBlock) {
    this.info = new DamageInfo(source, damage, DamageInfo.DamageType.THORNS);
    this.setValues(target, info);
    this.source = source;
    this.actionType = ActionType.DAMAGE;
    this.attackEffect = attackEffect;
    this.ignoreBlock = ignoreBlock;
    this.duration = 0.1F;
  }

  /**
   * The interesting logic.
   * Before applying damage, we remove block and all relevant powers.
   * They are re applied after damage is done.
   */
  private void dealDamage() {
    // Remove block & all relevant powers before damaging.
    int block = target.currentBlock;
    if (this.ignoreBlock) {
      target.currentBlock = 0;
    }

    List<AbstractPower> powers = new ArrayList<>();
    for (String powerID : IGNORED_POWERS) {
      AbstractPower power = this.target.getPower(powerID);
      if (power == null) continue;

      powers.add(power);
      this.target.powers.remove(power);
    }

    // We can't ignore Sharp Hide (Guardian) the same way as the rest of the powers,
    // because the damage action from Sharp Hide gets added when an attack is played.
    // Before this action finishes we can modify the existing state of the queue,
    // and remove the damage action added from Sharp Hide.
    int removeIndex = -1;
    for (int i = 0; i < AbstractDungeon.actionManager.actions.size(); ++i) {
      AbstractGameAction action = AbstractDungeon.actionManager.actions.get(i);
      // Look for a damage action targeting the player.
      if (!(action instanceof DamageAction)) continue;

      DamageAction damageAction = (DamageAction) action;
      if (damageAction.target != AbstractDungeon.player) continue;

      removeIndex = i;
      break;
    }

    if (removeIndex != -1) {
      AbstractDungeon.actionManager.actions.remove(removeIndex);
    }

    // Damage 'em.
    this.target.damage(this.info);

    // Put everything back.
    if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) return;

    if (this.ignoreBlock) {
      target.currentBlock = block;
    }
    this.target.powers.addAll(powers);
  }

  /**
   * 100% copy pasted from DamageAction.
   */
  @Override
  public void update() {
    if (this.shouldCancelAction() && this.info.type != DamageInfo.DamageType.THORNS) {
      this.isDone = true;
    } else {
      if (this.duration == 0.1F) {
        if (this.info.type != DamageInfo.DamageType.THORNS && (this.info.owner.isDying || this.info.owner.halfDead)) {
          this.isDone = true;
          return;
        }

        AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect, false));
      }

      this.tickDuration();
      if (this.isDone) {
        if (this.attackEffect == AttackEffect.POISON) {
          this.target.tint.color.set(Color.CHARTREUSE.cpy());
          this.target.tint.changeColor(Color.WHITE.cpy());
        } else if (this.attackEffect == AttackEffect.FIRE) {
          this.target.tint.color.set(Color.RED);
          this.target.tint.changeColor(Color.WHITE.cpy());
        }

        this.dealDamage();

        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
          AbstractDungeon.actionManager.clearPostCombatActions();
        }
      }
    }
  }
}
