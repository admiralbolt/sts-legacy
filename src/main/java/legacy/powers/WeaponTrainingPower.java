package legacy.powers;

import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.equipment.weapons.LegacyWeapon;

/**
 * At the end of turn, gain block equal to dexterity.
 */
public class WeaponTrainingPower extends LegacyPower {
  public static final String POWER_ID = "legacy:weapon_training_power";

  public WeaponTrainingPower(AbstractCreature owner, int amount) {
    super(POWER_ID, owner, amount, PowerType.BUFF);
  }

  @Override
  public void onPlayCard(AbstractCard card, AbstractMonster m) {
    if (!(card instanceof LegacyWeapon)) return;

    this.addToBot(new ModifyDamageAction(card.uuid, this.amount));
  }
}
