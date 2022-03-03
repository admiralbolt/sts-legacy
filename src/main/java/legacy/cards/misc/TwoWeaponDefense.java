package legacy.cards.misc;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import legacy.cards.LegacyCard;
import legacy.powers.FlurryPower;

/**s
 * A block card that scales with flurry & dex.
 */
public class TwoWeaponDefense extends LegacyCard {

  public static final String ID = "legacy:two_weapon_defense";
  private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 1;

  public TwoWeaponDefense() {
    super(ID, CARD_STRINGS.NAME, COST, CARD_STRINGS.DESCRIPTION, LegacyCardType.MISC, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

    this.baseBlock = this.block = 5;
    this.baseMagicNumber = this.magicNumber = 1;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeMagicNumber(1);
    this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new GainBlockAction(p, p, this.block));
  }

  @Override
  protected void applyPowersToBlock() {
    super.applyPowersToBlock();

    AbstractPower flurry = AbstractDungeon.player.getPower(FlurryPower.POWER_ID);
    if (flurry == null) return;

    this.block += flurry.amount * this.magicNumber;
  }
}
