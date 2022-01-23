package legacy.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.beyond.GiantHead;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.SlowPower;

/**
 * Currently, this is a straight up rip off from the runesmith's IceCold power.
 * Credit where it's due: https://github.com/PureStream/Runesmith.
 *
 * Reduces damage equal to stacks. The target also receives the Slow debuff while frost is applied.
 */
public class FrostPower extends AbstractPower {

  public static final String POWER_ID = "legacy:frost";
  public static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

  public FrostPower(AbstractCreature owner, int amount) {
    this.ID = POWER_ID;
    this.name = POWER_STRINGS.NAME;
    this.owner = owner;
    this.amount = amount;
    this.img = ImageMaster.loadImage("legacy/images/powers/placeholder_power32.png");
    this.type = PowerType.DEBUFF;
    this.updateDescription();
  }

  @Override
  public void onInitialApplication() {
    super.onInitialApplication();

    AbstractPower slow = this.owner.getPower("Slow");
    System.out.println("INITIAL APPLICATION SLOW");
    System.out.println(slow);
    System.out.println("SLOW");
    if (slow == null) AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new SlowPower(this.owner, 0)));
  }

  // Remove the slow power after thawing.
  @Override
  public void onRemove() {
    super.onRemove();

    // Giant Head shouldn't be unslowed from getting thawed. :)
    if (this.owner instanceof GiantHead) return;

    AbstractPower slow = this.owner.getPower("Slow");
    if (slow != null) AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, slow));
  }

  public void atEndOfTurn(boolean isPlayer) {
    flash();
    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, "legacy:frost", 1));
  }

  public float atDamageGive(float damage, DamageInfo.DamageType type) {
    return type == DamageInfo.DamageType.NORMAL ? damage - (float)this.amount : damage;
  }

  public void updateDescription() {
    // On the last turn it should say "turn" instead of "turns". Hence the weird ternary operator accessing
    // descriptions.
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[(this.amount == 1) ? 3 : 2];
  }

}
