package legacy.base_classes;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import legacy.characters.TheAdventurer;

public class WizardLevelUpChoice extends LevelUpChoiceCard {

  public static final String ID = "legacy:level_up_choice_wizard";
  public static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

  public WizardLevelUpChoice() {
    super(ID, CARD_STRINGS, "wizard.png");
  }

  @Override
  public void onChoseThisOption() {
    if (!(AbstractDungeon.player instanceof TheAdventurer)) return;

    ((TheAdventurer) AbstractDungeon.player).levelUp(Wizard.ID);
  }

  @Override
  public AbstractCard makeCopy() {
    return new WizardLevelUpChoice();
  }
}
