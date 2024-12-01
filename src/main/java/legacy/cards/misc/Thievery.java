package legacy.cards.misc;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.LegacyCard;
import legacy.powers.LegacyThieveryPower;

/**
 * Whenever you deal unblocked attack damage, gain gold!
 */
public class Thievery extends LegacyCard {

  public static final String ID = "legacy:thievery";
  public static final int COST = 2;

  public Thievery() {
    super(ID, COST, LegacyCardType.MISC, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF, new StatRequirements(0, 2, 0));

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
    this.addToBot(new ApplyPowerAction(p, p, new LegacyThieveryPower(p, this.magicNumber)));
  }
}
