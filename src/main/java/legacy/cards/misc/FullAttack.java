package legacy.cards.misc;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import legacy.cards.LegacyCard;

/**
 * Discard all non attack cards, draw 3 attacks, set the cost of cards in hand to 0.
 */
public class FullAttack extends LegacyCard {

  public static final String ID = "legacy:full_attack";
  private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 2;

  public FullAttack() {
    super(ID, CARD_STRINGS.NAME, COST, CARD_STRINGS.DESCRIPTION, LegacyCardType.MISC, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);

    this.baseMagicNumber = this.magicNumber = 3;
    this.exhaust = true;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeMagicNumber(1);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    for (AbstractCard card : p.hand.group) {
      if (card.type == CardType.ATTACK) continue;

      this.addToBot(new DiscardSpecificCardAction(card));
    }
    this.addToBot(new DrawPileToHandAction(3, CardType.ATTACK));
    this.addToBot(new ApplyPowerAction(p, p, new NoDrawPower(p), 1));
    for (AbstractCard card : AbstractDungeon.player.hand.group) {
      if (card.type != CardType.ATTACK) continue;

      card.setCostForTurn(0);
    }
  }
}
