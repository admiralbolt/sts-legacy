package legacy.cards.prestige_classes;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.powers.FrenziedBerserkerPower;

/**
 * RAAAAAGE.
 *
 * What would happen if you put limit break and eruption on the same card?
 * Good things, probably.
 */
public class FrenziedBerserker extends PrestigeClass {

  public static final String ID = "legacy:frenzied_berserker";
  public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 2;

  public FrenziedBerserker() {
    super(ID, COST, CardRarity.RARE, new StatRequirements(6, 0, 0));

    this.baseMagicNumber = this.magicNumber = 2;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeMagicNumber(1);
    this.rawDescription = this.statRequirements.requirementsString() + cardStrings.UPGRADE_DESCRIPTION;
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    addToBot(new ApplyPowerAction(p, p, new FrenziedBerserkerPower(p, this.magicNumber)));
  }
}
