package legacy.ui;

import basemod.TopPanelItem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import legacy.characters.TheAdventurer;
import legacy.util.TextureLoader;

public class TopPanelXPItem extends TopPanelItem {

  private static final Texture ICON = TextureLoader.getTexture("legacy/images/ui/xp_icon.png");
  public static final String ID = "legacy:top_panel_xp_item";
  public static final Color BASE_COLOR = new Color(0.2549f, 0.4117f, 0.882f, 1.0f);
  private float flashTimer = 1.0f;

  public TopPanelXPItem() {
    super(ICON, ID);


  }

  @Override
  public void setX(float x) {
    this.x = x - 100;
    this.hitbox.x = x - 100;
  }

  @Override
  protected void onHover() {
    return;
  }

  @Override
  protected void onUnhover() {
    return;
  }

  @Override
  protected void onClick() {
    return;
  }

  @Override
  public void render(SpriteBatch sb) {
    super.render(sb);

    if (!(AbstractDungeon.player instanceof TheAdventurer)) return;

    Color textColor = BASE_COLOR.cpy();
    TheAdventurer adventurer = (TheAdventurer) AbstractDungeon.player;
    if (adventurer.xp >= adventurer.nextLevelXp) {
      float flashScale = Math.min(Math.max(this.flashTimer - 1, 0f), 1f);
      textColor.r += flashScale * 0.15f;
      textColor.g += flashScale * 0.15f;
      textColor.b += flashScale * 0.15f;
      textColor.clamp();

      this.flashTimer -= 0.017f;
      if (this.flashTimer <= 0) {
        this.flashTimer = 2.2f;
      }
    }

    FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, adventurer.xp + "/" + adventurer.nextLevelXp, this.x + 70.0F * Settings.scale, Settings.isMobile ? (float)Settings.HEIGHT - 36.0F * Settings.scale : (float)Settings.HEIGHT - 24.0F * Settings.scale, textColor);
  }
}
