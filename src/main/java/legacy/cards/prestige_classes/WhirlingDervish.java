package legacy.cards.prestige_classes;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.powers.WhirlingDervishPower;

/**
 * A class specializing in two weapon fighting.
 *
 * Double the amount of flurry generated.
 * Draw a card after playing (5 / 4) attacks in a turn.
 */
public class WhirlingDervish extends PrestigeClass {

  public static final String ID = "legacy:whirling_dervish";
  public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 2;

  public WhirlingDervish() {
    super(ID, cardStrings, COST, CardRarity.RARE, 2, 4, 0);

    this.magicNumber = this.baseMagicNumber = 5;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeMagicNumber(-1);
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    addToBot(new ApplyPowerAction(p, p, new WhirlingDervishPower(p, this.magicNumber)));
  }
}
