package legacy.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import legacy.LegacyMod;

public class LegacyTwoAmountPower extends TwoAmountPower {

  public final PowerStrings powerStrings;

  public LegacyTwoAmountPower(String id, AbstractCreature owner, int amount, int amount2, PowerType type) {
    this.ID = id;
    this.powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    this.name = this.powerStrings.NAME;
    this.owner = owner;
    this.amount = amount;
    this.amount2 = amount2;
    // Load low and hi-def versions of the power icon.
    this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("legacy/images/powers/" + LegacyMod.getNameFromId(id) + "_32.png"), 0, 0, 32, 32);
    this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("legacy/images/powers/" + LegacyMod.getNameFromId(id) + "_64.png"), 0, 0, 64, 64);
    this.type = type;
    this.updateDescription();
  }

  // This is mostly just a guess here, but a lot of powers have descriptions that are like:
  // BLOCK_OF_TEXT + amount + BLOCK_OF_TEXT.
  @Override
  public void updateDescription() {
    this.description = this.powerStrings.DESCRIPTIONS[0] + this.amount + this.powerStrings.DESCRIPTIONS[1];
  }

}
