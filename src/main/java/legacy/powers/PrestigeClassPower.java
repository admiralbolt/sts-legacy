package legacy.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import legacy.LegacyMod;
import legacy.util.TextureLoader;

/**
 * Powers for prestige classes.
 *
 * Main thing to note here, is that only one prestige class *should* be active at a time.
 * Unless of course you are gestalt. :)
 */
public class PrestigeClassPower extends AbstractPower {

  public final PowerStrings powerStrings;

  public PrestigeClassPower(String id, AbstractPlayer owner) {
    this.ID = id;
    this.powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    this.owner = owner;
    this.amount = 1;
    this.type = PowerType.BUFF;
    this.img = TextureLoader.getTexture("legacy/images/powers/prestige_classes/" + LegacyMod.getNameFromId(id) + ".png");
  }

  @Override
  public void onInitialApplication() {
    // Remove other prestige class powers after playing a new one.
    for (AbstractPower power : this.owner.powers) {
      if (!(power instanceof PrestigeClassPower)) continue;

      addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, power));
    }
  }
}
