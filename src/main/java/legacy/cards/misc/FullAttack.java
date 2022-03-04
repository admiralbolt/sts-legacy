package legacy.cards.misc;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import legacy.cards.LegacyCard;
import legacy.cards.equipment.weapons.LegacyWeapon;

/**s
 * Reduce the cost of all weapons in your hand by 1 for the turn.
 */
public class FullAttack extends LegacyCard {

  public static final String ID = "legacy:full_attack";
  private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 2;

  public FullAttack() {
    super(ID, CARD_STRINGS.NAME, COST, CARD_STRINGS.DESCRIPTION, LegacyCardType.MISC, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);

    this.baseMagicNumber = this.magicNumber = -1;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeMagicNumber(3);
    this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    for (AbstractCard card : AbstractDungeon.player.hand.group) {
      if (!(card instanceof LegacyWeapon)) continue;

      card.setCostForTurn(Math.max(0, card.cost - 1));
    }

    if (!this.upgraded) return;
    this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
    this.addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, this.magicNumber), this.magicNumber));
  }
}
