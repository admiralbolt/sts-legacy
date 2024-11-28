package legacy.base_classes;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.powers.*;
import legacy.util.TextureLoader;

/**
 * This is a pretty interesting hack to implement base classes.
 *
 * We implement them as blights, which renders them nicely below our relics. This idea is stolen from the Bladedancer
 * mod: https://github.com/RattusInVitaReali/Bladedancer.
 */
public class BaseClassBlight extends AbstractBlight {

  private final BlightStrings blightStrings;
  private final String powerId;

  public BaseClassBlight(String id, int level, BlightStrings blightStrings, String imageName, String powerId) {
    super(id, blightStrings.NAME, blightStrings.DESCRIPTION[0] + level + blightStrings.DESCRIPTION[1], "legacy/images/relics/base_classes/" + imageName + ".png", true);

    this.blightStrings = blightStrings;
    this.counter = level;
    this.powerId = powerId;
    this.img = TextureLoader.getTexture("legacy/images/relics/base_classes/" + imageName + ".png");
    this.outlineImg = TextureLoader.getTexture("legacy/images/relics/outline/base_class.png");
    this.updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = this.blightStrings.DESCRIPTION[0] + this.counter + this.blightStrings.DESCRIPTION[1];
    this.tips.clear();
    this.tips.add(new PowerTip(this.name, this.description));
    this.initializeTips();
  }

  @Override
  public void atBattleStart() {
    if (this.counter == 0) return;

    this.flash();
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, this.getPower(), this.counter));
  }

  // BaseMod.getPowerClass(String id) provides a way of getting powers by id, but rather than re-implementing my own
  // weird reflection constructor stuff I'm just gonna make it easy:
  private AbstractPower getPower() {
    switch (this.powerId) {
      case "Strength":
        return new StrengthPower(AbstractDungeon.player, this.counter);
      case "Dexterity":
        return new DexterityPower(AbstractDungeon.player, this.counter);
      case "Focus":
        return new FocusPower(AbstractDungeon.player, this.counter);
      default:
        // nice
        return new AccuracyPower(AbstractDungeon.player, 69);
    }
  }

  @Override
  public void stack() {
    this.counter++;
    this.updateDescription();
    this.flash();
  }
}
