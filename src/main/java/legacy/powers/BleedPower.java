package legacy.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

/**
 * Take damage at end of turn and on attack.
 */
public class BleedPower extends LegacyPower implements HealthBarRenderPower {

  public static final String POWER_ID = "legacy:bleed_power";
  public static final Color BLEED_POWER_COLOR = Color.valueOf("#881111");

  public BleedPower(AbstractCreature owner, int amount) {
    super(POWER_ID, owner, amount, PowerType.DEBUFF);
  }

  @Override
  public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
    if (info.type != DamageInfo.DamageType.NORMAL || info.owner != this.owner) return;

    this.flash();
    this.addToTop(new DamageAction(info.owner, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.POISON, true));
  }

  @Override
  public void atStartOfTurn() {
    if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) return;

    this.flashWithoutSound();
    this.addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.HP_LOSS)));
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    this.flash();
    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
  }

  @Override
  public int getHealthBarAmount() {
    return this.amount;
  }

  @Override
  public Color getColor() {
    return BLEED_POWER_COLOR;
  }
}
