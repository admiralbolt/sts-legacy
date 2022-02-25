package legacy.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.FocusPower;
import legacy.cards.spells.Spell;

/**
 * Whenever you play a spell gain 1 temporary focus.
 * Whenever you play a spell gain 1 energy.
 */
public class ArchMagePower extends PrestigeClassPower {

  public static final String POWER_ID = "legacy:arch_mage_power";

  // The amount of bonus temporary focus we've generated this turn.
  private int spellPower = 0;

  public ArchMagePower(AbstractPlayer owner) {
    super(POWER_ID, owner);

    this.updateDescription();
  }

  @Override
  public void atStartOfTurn() {
    this.spellPower = 0;
  }

  @Override
  public void onUseCard(AbstractCard card, UseCardAction action) {
    super.onUseCard(card, action);

    if (!(card instanceof Spell)) return;

    this.addToBot(new ApplyPowerAction(this.owner, this.owner, new FocusPower(this.owner, 1), 1));
    this.addToBot(new ApplyPowerAction(this.owner, this.owner, new LoseFocusPower(this.owner, 1), 1));
    this.addToBot(new GainEnergyAction(1));
    this.spellPower++;
  }

  @Override
  public void onRemove() {
    // Remove any temporary focus we've generated this turn.
    if (this.spellPower == 0) return;

    this.addToBot(new ApplyPowerAction(this.owner, this.owner, new FocusPower(this.owner, -this.spellPower), -this.spellPower));
    this.addToBot(new ApplyPowerAction(this.owner, this.owner, new LoseFocusPower(this.owner, -this.spellPower), -this.spellPower));
  }

}
