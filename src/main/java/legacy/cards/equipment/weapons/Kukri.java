package legacy.cards.equipment.weapons;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import legacy.cards.mods.traits.FinesseTrait;
import legacy.cards.mods.traits.FlurryTrait;
import legacy.patches.LegacyCardTags;

public class Kukri extends LegacyWeapon {

  public static final String ID = "legacy:kukri";
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 0;

  public Kukri() {
    super(ID, cardStrings, COST, CardRarity.COMMON, CardTarget.ENEMY, new FlurryTrait(), new FinesseTrait());

    this.tags.add(LegacyCardTags.FLURRY);
    this.baseDamage = this.damage = 2;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(2);
    this.initializeDescription();
  }

}

