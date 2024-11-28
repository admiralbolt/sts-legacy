package legacy.cards.equipment.weapons;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import legacy.LegacyMod;

public class Mace extends LegacyWeapon {

  public static final String ID = LegacyMod.makeID("mace");
  public static final int COST = 1;

  public Mace() {
    super(ID, COST, CardRarity.COMMON, CardTarget.ENEMY);

    this.baseDamage = this.damage = 5;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(3);
    this.initializeDescription();
  }

}

