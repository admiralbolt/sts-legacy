package legacy.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * Whirling Dervish Power doubles the amount of flurry you generate, and you draw a card every 5/4 attacks.
 */
public class WhirlingDervishPower extends PrestigeClassPower {

  public static final String POWER_ID = "legacy:whirling_dervish_power";

  private final int cardsForDraw;

  public WhirlingDervishPower(AbstractPlayer owner, int cardsForDraw) {
    super(POWER_ID, owner);

    this.cardsForDraw = cardsForDraw;
    this.amount = cardsForDraw;
    this.updateDescription();
  }

  @Override
  public void updateDescription() {
    // Pluralization here. "If you play 5 more attacks..." vs. "If you play 1 more attack..."
    this.description = this.powerStrings.DESCRIPTIONS[0] + this.amount + this.powerStrings.DESCRIPTIONS[(this.amount > 1) ? 2 : 1];
  }

  @Override
  public void onInitialApplication() {
    super.onInitialApplication();
  }

  @Override
  public void onUseCard(AbstractCard card, UseCardAction action) {
    // Every X atatcks played, we draw a card.
    if (card.type != AbstractCard.CardType.ATTACK) return;

    this.amount--;
    if (this.amount == 0) {
      this.flash();
      this.amount = this.cardsForDraw;
      addToBot(new DrawCardAction(AbstractDungeon.player, 1));
    }

    this.updateDescription();
  }

  @Override
  public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
    // Double the amount of flurry generated.
    if (!power.ID.equals(FlurryPower.POWER_ID)) return;

    if (target.hasPower(FlurryPower.POWER_ID)) {
      target.getPower(FlurryPower.POWER_ID).stackPower(power.amount);
    } else {
      power.stackPower(power.amount);
    }
  }

  @Override
  public void atStartOfTurn() {
    this.amount = this.cardsForDraw;
    this.updateDescription();
  }
}
