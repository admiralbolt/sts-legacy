package legacy.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import legacy.base_classes.Fighter;
import legacy.base_classes.LevelUpChoiceCard;
import legacy.base_classes.Rogue;
import legacy.base_classes.Wizard;

import java.util.ArrayList;

public class CampfireLevelUpEffect extends AbstractGameEffect {

  public static final String ID = "legacy:level_up_effect";
  private final Color screenColor = AbstractDungeon.fadeColor.cpy();

  public CampfireLevelUpEffect() {
    this.duration = 1.5F;
    this.screenColor.a = 0.0F;
    AbstractDungeon.overlayMenu.proceedButton.hide();
  }

  private void updateBlackScreenColor() {
    if (this.duration > this.startingDuration - 0.5F) {
      this.screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - (this.startingDuration - 0.5F)) * 2.0F);
    } else if (this.duration < 1.0F) {
      this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration);
    } else {
      this.screenColor.a = 1.0F;
    }
  }

  @Override
  public void update() {
    this.duration -= Gdx.graphics.getDeltaTime();
    this.updateBlackScreenColor();

    ArrayList<AbstractCard> levelUpCards = new ArrayList<>();
    levelUpCards.add(new LevelUpChoiceCard(Fighter.ID, "Fighter"));
    levelUpCards.add(new LevelUpChoiceCard(Rogue.ID, "Rogue"));
    levelUpCards.add(new LevelUpChoiceCard(Wizard.ID, "Wizard"));
    AbstractDungeon.cardRewardScreen.chooseOneOpen(levelUpCards);

    this.isDone = true;
    ((RestRoom)AbstractDungeon.getCurrRoom()).fadeIn();
    AbstractRoom.waitTimer = 0.0F;
    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
  }

  @Override
  public void render(SpriteBatch spriteBatch) {

  }

  @Override
  public void dispose() {

  }
}
