package legacy.cards.equipment.weapons;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import legacy.LegacyMod;

public class Mace extends LegacyWeapon {

  public static final String ID = LegacyMod.makeID("mace");
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 1;

  public Mace() {
    super(ID, cardStrings, COST, CardRarity.COMMON, CardTarget.ENEMY);
  }

}

