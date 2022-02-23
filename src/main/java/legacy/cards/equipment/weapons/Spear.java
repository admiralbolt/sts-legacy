package legacy.cards.equipment.weapons;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import legacy.LegacyMod;
import legacy.cards.mods.traits.FinesseTrait;
import legacy.cards.mods.traits.TwoHandedTrait;

public class Spear extends LegacyWeapon {

  public static final String ID = LegacyMod.makeID("spear");
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 2;

  public Spear() {
    super(ID, cardStrings, COST, CardRarity.COMMON, CardTarget.ENEMY, new TwoHandedTrait(), new FinesseTrait());

    this.baseDamage = this.damage = 9;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(5);
    this.initializeDescription();
  }

}

