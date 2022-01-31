package legacy.base_classes;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import legacy.characters.TheAdventurer;

public class RogueLevelUpChoice extends LevelUpChoiceCard {

  public static final String ID = "legacy:level_up_choice_rogue";
  public static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

  public RogueLevelUpChoice() {
    super(ID, CARD_STRINGS, "rogue.png");
  }

  @Override
  public void onChoseThisOption() {
    if (!(AbstractDungeon.player instanceof TheAdventurer)) return;

    ((TheAdventurer) AbstractDungeon.player).levelUp(Rogue.ID);
  }

  @Override
  public AbstractCard makeCopy() {
    return new RogueLevelUpChoice();
  }
}
