package legacy.base_classes;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

/**
 * Gain 1 strength for each level of fighter you have.
 */
public class Fighter extends BaseClassBlight {

  public static final String ID = "legacy:fighter_class_blight";
  public static final BlightStrings BLIGHT_STRINGS = CardCrawlGame.languagePack.getBlightString(ID);

  public Fighter(int level) {
    super(ID, level, BLIGHT_STRINGS, "fighter", StrengthPower.POWER_ID);
  }

}
