package legacy.ui;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import legacy.util.TextureLoader;

public class LevelUpCampfireOption extends AbstractCampfireOption {

  public static final String ID = "legacy:level_up";
  public static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ID);

  public LevelUpCampfireOption() {
    this.img = TextureLoader.getTexture("legacy/images/ui/campfire/level_up.png");
    this.label = UI_STRINGS.TEXT[0];
    this.description = UI_STRINGS.TEXT[1];
  }

  @Override
  public void useOption() {
    if (!this.usable) return;

    AbstractDungeon.effectList.add(new CampfireLevelUpEffect());

  }
}
