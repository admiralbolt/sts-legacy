package legacy.relics;

import basemod.abstracts.CustomRelic;
import legacy.LegacyMod;
import legacy.util.TextureLoader;

public abstract class LegacyRelic extends CustomRelic {

  public static String getImagePath(String id) {
    return LegacyMod.MOD_ID + "/images/relics/" + LegacyMod.getNameFromId(id) + ".png";
  }

  public static String getOutlinePath(String id) {
    return LegacyMod.MOD_ID + "/images/relics/outline/" + LegacyMod.getNameFromId(id) + ".png";
  }

  public LegacyRelic(String id, RelicTier tier, LandingSound sfx) {
    this(id, tier, sfx, true);
  }

  public LegacyRelic(String id, RelicTier tier, LandingSound sfx, boolean hasCharges) {
    super(id, TextureLoader.getTexture(getImagePath(id)), TextureLoader.getTexture(getOutlinePath(id)), tier, sfx);

    if (hasCharges) {
      this.counter = RelicUtils.getCharge(id);
    }
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0];
  }

  public void incrementCharge() {
    this.flash();
    this.counter++;
    RelicUtils.incrementCharge(this.relicId);
  }

  public void incrementCharge(int amount) {
    this.flash();
    this.counter += amount;
    RelicUtils.incrementCharge(this.relicId);
  }

}
