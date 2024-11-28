package legacy.cards.spells;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * Scry 3 then draw a card.
 * Upgrade: Scry 3 then draw 2 cards.
 */
public class Augury extends Spell {

  public static final String ID = "legacy:augury";
  public static final int COST = 1;

  public Augury() {
    super(ID, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE, SpellSchool.DIVINATION, 3);

    this.baseMagicNumber = this.magicNumber = 3;
    this.baseMagicNumberTwo = this.magicNumberTwo = 1;
  }

  @Override
  public void applyPowers() {
    super.applyPowers(true);
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeMagicNumberTwo(1);
    this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    this.addToBot(new ScryAction(this.magicNumber));
    this.addToBot(new DrawCardAction(this.magicNumberTwo));
  }
}
