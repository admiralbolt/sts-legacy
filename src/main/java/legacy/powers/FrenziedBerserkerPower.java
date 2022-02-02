package legacy.powers;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import legacy.actions.MultiplyStrengthAction;

/**
 * Frenzied Beserker Power grants x2 (or x3) strength, and wrath.
 */
public class FrenziedBerserkerPower extends PrestigeClassPower {

  public static final String POWER_ID = "legacy:frenzied_berserker_power";

  private int strengthIncrease;
  private final int strengthMultiplier;

  public FrenziedBerserkerPower(AbstractPlayer owner, int strengthMultiplier) {
    super(POWER_ID, owner);

    this.strengthMultiplier = strengthMultiplier;
    this.updateDescription();
  }

  @Override
  public void updateDescription() {
    // Triple your strength vs. double your strength.
    this.description = this.powerStrings.DESCRIPTIONS[(this.strengthMultiplier > 2) ? 1 : 0];
  }

  @Override
  public void onInitialApplication() {
    super.onInitialApplication();
    // Increase strength when we gain the frenzied beserker power.
    // I wrote a MultiplyStrengthAction, but I still have to do the calculation here for tracking anyway :/
    AbstractPower strength = AbstractDungeon.player.getPower("Strength");
    int strengthAmount = (strength == null) ? 0 : strength.amount;
    this.strengthIncrease = strengthAmount * (this.strengthMultiplier - 1);

    addToBot(new VFXAction(this.owner, new InflameEffect(this.owner), 1.0F));
    addToBot(new MultiplyStrengthAction(this.owner, this.strengthMultiplier));
    addToBot(new ChangeStanceAction("Wrath"));
  }

  @Override
  public void onRemove() {
    // Remove the increased strength, exit wrath if we are in it.
    addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -this.strengthIncrease), -this.strengthIncrease));
    if (((AbstractPlayer) this.owner).stance.ID.equals("Wrath")) {
     addToBot(new ChangeStanceAction("Neutral"));
    }
  }

}
