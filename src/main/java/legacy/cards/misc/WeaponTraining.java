package legacy.cards.prestige_classes;

import basemod.cardmods.EtherealMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.LegacyCard;
import legacy.powers.ArchMagePower;
import legacy.powers.WeaponTrainingPower;

/**
 * Whenever you play a weapon, increase its damage this combat by 1.
 * Upgrades to 2.
 */
public class WeaponTraining extends LegacyCard {

  public static final String ID = "legacy:weapon_training";
  public static final int COST = 2;

  public WeaponTraining() {
    super(ID, COST, LegacyCardType.MISC, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF, new StatRequirements(2, 0, 0));

    this.magicNumber = this.baseMagicNumber = 1;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeMagicNumber(1);
    this.upgradeName();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    addToBot(new ApplyPowerAction(p, p, new WeaponTrainingPower(p, this.magicNumber)));
  }
}
