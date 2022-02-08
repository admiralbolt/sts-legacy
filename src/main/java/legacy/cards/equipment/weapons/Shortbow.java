package legacy.cards.equipment.weapons;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import legacy.LegacyMod;
import legacy.cards.mods.traits.FinesseTrait;
import legacy.cards.mods.traits.RangedTrait;

public class Shortbow extends LegacyWeapon {

  public static final String ID = "legacy:shortbow";
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  private static final int COST = 1;

  public Shortbow() {
    super(ID, cardStrings, COST, CardRarity.COMMON, CardTarget.ENEMY, new FinesseTrait(), new RangedTrait());
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(3);
    this.initializeDescription();
  }

}

