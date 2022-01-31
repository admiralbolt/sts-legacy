package legacy.base_classes;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import legacy.characters.TheAdventurer;

public class FighterLevelUpChoice extends LevelUpChoiceCard {

  public static final String ID = "legacy:level_up_choice_fighter";
  public static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

  public FighterLevelUpChoice() {
    super(ID, CARD_STRINGS, "fighter.png");
  }

  @Override
  public void onChoseThisOption() {
    if (!(AbstractDungeon.player instanceof TheAdventurer)) return;

    ((TheAdventurer) AbstractDungeon.player).levelUp(Fighter.ID);
  }

  @Override
  public AbstractCard makeCopy() {
    return new FighterLevelUpChoice();
  }
}
