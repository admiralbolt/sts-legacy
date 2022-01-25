package legacy.cards.weapons;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import legacy.LegacyMod;
import legacy.cards.mods.traits.FinesseTrait;
import legacy.cards.mods.traits.RangedTrait;

public class Whip extends LegacyWeapon {

  public static final String ID = LegacyMod.makeID("whip");
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 1;

  public Whip() {
    super(ID, cardStrings, COST, CardRarity.UNCOMMON, CardTarget.ENEMY, new FinesseTrait(), new RangedTrait());
  }

}

