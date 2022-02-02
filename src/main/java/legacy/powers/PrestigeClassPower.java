package legacy.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
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
    // Don't forget to include the name... Just wasted 1 hour on this fucking bug.
    this.name = this.powerStrings.NAME;
    // Default the description to the first description entry in powerstrings.
    this.description = this.powerStrings.DESCRIPTIONS[0];
    this.owner = owner;
    // Prestige class powers shouldn't stack.
    this.amount = -1;
    this.type = PowerType.BUFF;
    this.img = ImageMaster.loadImage("legacy/images/powers/prestige_classes/" + LegacyMod.getNameFromId(id) + ".png");
    System.out.println("wtf");
    System.out.println(this.img);
  }

  @Override
  public void onInitialApplication() {
    // Remove other prestige class powers after playing a new one.
    // We need to make sure we don't remove the power we are currently playing though.
    for (AbstractPower power : this.owner.powers) {
      if (!(power instanceof PrestigeClassPower)) continue;
      if (power.ID.equals(this.ID)) continue;

      addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, power));
    }
  }
}
