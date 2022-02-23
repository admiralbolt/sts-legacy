package legacy.cards.equipment.gear;

import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import legacy.cards.LegacyCard;

/**
 *
 */
public class TreasureMap extends LegacyCard {

  public static final String ID = "legacy:treasure_map";
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 1;

  public TreasureMap() {
    super(ID, cardStrings.NAME, COST, cardStrings.DESCRIPTION, LegacyCardType.GEAR, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

    this.baseMagicNumber = this.magicNumber = 7;
    this.baseMagicNumberTwo = this.magicNumberTwo = 1;
    this.exhaust = true;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeMagicNumber(3);
    this.upgradeMagicNumberTwo(1);
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new ScryAction(this.magicNumberTwo));
    this.addToBot(new GainGoldAction(this.magicNumber));
    for (int i = 0; i < this.magicNumber; ++i) {
      AbstractDungeon.effectList.add(new GainPennyEffect(p, p.hb.cX, p.hb.cY, p.hb.cX, p.hb.cY, true));
    }
  }

}
