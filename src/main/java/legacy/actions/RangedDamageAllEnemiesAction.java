package legacy.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * A clone of DamageAllEnemiesAction, but for ranged damage.
 * Wish there was a better way but I'm dumb :/
 */
public class RangedDamageAllEnemiesAction extends AbstractGameAction {

  public int[] damage;
  private boolean firstFrame;
  private boolean ignoreBlock;

  public RangedDamageAllEnemiesAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect, boolean ignoreBlock, boolean isFast) {
    this.firstFrame = true;
    this.source = source;
    this.damage = amount;
    this.actionType = ActionType.DAMAGE;
    this.damageType = type;
    this.attackEffect = effect;
    if (isFast) {
      this.duration = Settings.ACTION_DUR_XFAST;
    } else {
      this.duration = Settings.ACTION_DUR_FAST;
    }
    this.ignoreBlock = ignoreBlock;
  }

  public RangedDamageAllEnemiesAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect, boolean ignoreBlock) {
    this(source, amount, type, effect, ignoreBlock, true);
  }

  /**
   * The interesting logic.
   * Before applying damage, we remove block and all relevant powers.
   * They are re applied after damage is done.
   */
  private void dealDamage(AbstractMonster target, int damage) {
    // Remove block & all relevant powers before damaging.
    int block = target.currentBlock;
    if (this.ignoreBlock) {
      target.currentBlock = 0;
    }

    List<AbstractPower> powers = new ArrayList<>();
    for (String powerID : RangedDamageAction.IGNORED_POWERS) {
      AbstractPower power = target.getPower(powerID);
      if (power == null) continue;

      powers.add(power);
      target.powers.remove(power);
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
    target.damage(new DamageInfo(this.source, damage, this.damageType));

    // Put everything back.
    if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) return;

    if (this.ignoreBlock) {
      target.currentBlock = block;
    }
    target.powers.addAll(powers);
  }

  public void update() {
    if (this.firstFrame) {
      boolean playedMusic = false;
      int temp = AbstractDungeon.getCurrRoom().monsters.monsters.size();

      for(int i = 0; i < temp; ++i) {
        if (!((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).isDying && ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).currentHealth > 0 && !((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).isEscaping) {
          if (playedMusic) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cX, ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cY, this.attackEffect, true));
          } else {
            playedMusic = true;
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cX, ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cY, this.attackEffect));
          }
        }
      }

      this.firstFrame = false;
    }

    this.tickDuration();
    if (this.isDone) {
      for(AbstractPower p : AbstractDungeon.player.powers) {
        p.onDamageAllEnemies(this.damage);
      }

      int temp = AbstractDungeon.getCurrRoom().monsters.monsters.size();

      for(int i = 0; i < temp; ++i) {
        if (!((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).isDeadOrEscaped()) {
          if (this.attackEffect == AttackEffect.POISON) {
            ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).tint.color.set(Color.CHARTREUSE);
            ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).tint.changeColor(Color.WHITE.cpy());
          } else if (this.attackEffect == AttackEffect.FIRE) {
            ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).tint.color.set(Color.RED);
            ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).tint.changeColor(Color.WHITE.cpy());
          }

          this.dealDamage(AbstractDungeon.getCurrRoom().monsters.monsters.get(i), this.damage[i]);
        }
      }

      if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
        AbstractDungeon.actionManager.clearPostCombatActions();
      }

      if (!Settings.FAST_MODE) {
        this.addToTop(new WaitAction(0.1F));
      }
    }
  }

}
