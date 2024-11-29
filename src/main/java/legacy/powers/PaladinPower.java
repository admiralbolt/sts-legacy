package legacy.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import legacy.cards.equipment.armor.LegacyArmor;
import legacy.cards.equipment.weapons.LegacyWeapon;
import legacy.cards.spells.Spell;

/**
 * Whenever you cast a spell, heal 1 hp.
 * Whenever you play a weapon, gain 1 steadfast.
 * Whenever you play an armor, gain 1 vigor.
 */
public class PaladinPower extends PrestigeClassPower {

  public static final String POWER_ID = "legacy:paladin_power";
  private final int vigorAmount;

  public PaladinPower(AbstractPlayer owner, int vigorAmount) {
    super(POWER_ID, owner);

    this.vigorAmount = vigorAmount;
    this.updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = this.powerStrings.DESCRIPTIONS[0] + this.powerStrings.DESCRIPTIONS[1] + this.vigorAmount + this.powerStrings.DESCRIPTIONS[2] + this.powerStrings.DESCRIPTIONS[3] + this.vigorAmount + this.powerStrings.DESCRIPTIONS[4];
  }

  @Override
  public void onUseCard(AbstractCard card, UseCardAction action) {
    super.onUseCard(card, action);

    if (card instanceof Spell) {
      this.addToBot(new HealAction(this.owner, this.owner, 1));
    } else if (card instanceof LegacyWeapon) {
      this.addToBot(new ApplyPowerAction(this.owner, this.owner, new SteadfastPower(this.owner, this.vigorAmount), this.vigorAmount));
    } else if (card instanceof LegacyArmor) {
      this.addToBot(new ApplyPowerAction(this.owner, this.owner, new VigorPower(this.owner, this.vigorAmount), this.vigorAmount));
    }
  }

}
