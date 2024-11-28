package legacy.cards.misc;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.LegacyCard;
import legacy.patches.LegacyCardTags;
import legacy.powers.StealthPower;

/**
 * Gain 2 stacks of stealth. Upgrades to gain 3 stacks of stealth.
 */
public class Hide extends LegacyCard {

  public static final String ID = "legacy:hide";
  private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 1;

  public Hide() {
    super(ID, CARD_STRINGS.NAME, COST, CARD_STRINGS.DESCRIPTION, LegacyCardType.MISC, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF, new StatRequirements(0, 1, 0));

    this.tags.add(LegacyCardTags.ENTERS_STEALTH);
    this.magicNumber = this.baseMagicNumber = 2;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeMagicNumber(1);
    this.upgradeName();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    addToBot(new ApplyPowerAction(p, p, new StealthPower(p, this.magicNumber)));
  }
}
