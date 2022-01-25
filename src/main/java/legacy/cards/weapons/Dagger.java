package legacy.cards.weapons;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import legacy.LegacyMod;
import legacy.cards.mods.traits.FlurryTrait;

public class Dagger extends LegacyWeapon {

  public static final String ID = LegacyMod.makeID("dagger");
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 0;

  public Dagger() {
    super(ID, cardStrings, COST, CardRarity.COMMON, CardTarget.ENEMY, new FlurryTrait());
  }

}

