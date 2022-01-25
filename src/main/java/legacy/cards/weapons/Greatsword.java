package legacy.cards.weapons;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import legacy.LegacyMod;
import legacy.cards.mods.traits.TwoHandedTrait;

public class Greatsword extends LegacyWeapon {

  public static final String ID = LegacyMod.makeID("greatsword");
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 2;

  public Greatsword() {
    super(ID, cardStrings, COST, CardRarity.COMMON, CardTarget.ENEMY, new TwoHandedTrait());
  }

}

