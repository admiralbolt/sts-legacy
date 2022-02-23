package legacy.cards.equipment.gear;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.LegacyCard;

/**
 * P L S S L P.
 *
 * Heal for 7% of your max hp. End the turn.
 * Upgrade: Heal for 10% of your max hp. End the turn.
 */
public class Bedroll extends LegacyCard {

  public static final String ID = "legacy:bedroll";
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 2;

  public Bedroll() {
    super(ID, cardStrings.NAME, COST, cardStrings.DESCRIPTION, LegacyCardType.GEAR, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

    this.baseMagicNumber = this.magicNumber = 7;
    this.exhaust = true;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeMagicNumber(3);
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new HealAction(p, p, (int) (p.maxHealth * this.magicNumber / 100.0)));
    this.addToBot(new PressEndTurnButtonAction());
  }

}
