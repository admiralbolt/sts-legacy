package legacy.cards.prestige_classes;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.powers.PaladinPower;

/**
 * Sword & board & magic!
 */
public class Paladin extends PrestigeClass {

  public static final String ID = "legacy:paladin";
  public static final int COST = 2;

  public Paladin() {
    super(ID, COST, CardRarity.RARE, new StatRequirements(2, 0, 2));

    this.magicNumber = this.baseMagicNumber = 1;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeMagicNumber(1);
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    addToBot(new ApplyPowerAction(p, p, new PaladinPower(p, this.magicNumber)));
  }
}
