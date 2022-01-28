package legacy.base_classes;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;

/**
 * Gain 1 strength for each level of fighter you have.
 */
public class Rogue extends BaseClassBlight {

  public static final String ID = "legacy:rogue_class_blight";
  public static final BlightStrings BLIGHT_STRINGS = CardCrawlGame.languagePack.getBlightString(ID);

  public Rogue(int level) {
    super(ID, level, BLIGHT_STRINGS, "rogue", DexterityPower.POWER_ID);
  }

}
