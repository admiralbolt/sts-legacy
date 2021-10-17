package legacy.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FlurryPower extends AbstractPower {

  public static final String POWER_ID = "legacy:flurry";
  public static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

  public FlurryPower(AbstractCreature owner, int amount) {
    this.ID = POWER_ID;
    this.name = POWER_STRINGS.NAME;
    this.owner = owner;
    this.amount = amount;
    this.img = ImageMaster.loadImage("legacy/images/powers/placeholder_power32.png");
    this.type = AbstractPower.PowerType.BUFF;
    this.updateDescription();
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
  }

  @Override
  public void updateDescription() {
    this.description = POWER_STRINGS.DESCRIPTIONS[0] + this.amount + POWER_STRINGS.DESCRIPTIONS[1];
  }

}
