package legacy.cards.misc;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.LegacyCard;

/**s
 * The most OP movement option in the game.
 */
public class FiveFootStep extends LegacyCard {

  public static final String ID = "legacy:five_foot_step";
  public static final int COST = 0;

  public FiveFootStep() {
    super(ID, COST, LegacyCardType.MISC, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);

    this.baseBlock = this.block = 3;
    this.magicNumber = this.baseMagicNumber = 1;
    this.exhaust = true;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.exhaust = false;
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new GainBlockAction(p, p, this.block));
    this.addToBot(new DrawCardAction(this.magicNumber));
  }
}
