package legacy.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class GainTemporaryStrengthNextTurnPower extends AbstractPower {

  public static final String POWER_ID = "legacy:next_turn_tmp_strength";
  public static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

  public GainTemporaryStrengthNextTurnPower(AbstractCreature owner, int amount) {
    this.ID = POWER_ID;
    this.name = POWER_STRINGS.NAME;
    this.owner = owner;
    this.amount = amount;
    this.img = ImageMaster.loadImage("legacy/images/powers/placeholder_power32.png");
    this.type = PowerType.BUFF;
    this.updateDescription();
  }

  public void updateDescription() {
    this.description = POWER_STRINGS.DESCRIPTIONS[0] + this.amount + POWER_STRINGS.DESCRIPTIONS[1];
  }

    public void atStartOfTurnPostDraw() {
      this.flash();
      this.addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
      this.addToBot(new ApplyPowerAction(this.owner, this.owner, new LoseStrengthPower(this.owner, this.amount), this.amount));
      this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, GainTemporaryStrengthNextTurnPower.POWER_ID));
    }
  }
