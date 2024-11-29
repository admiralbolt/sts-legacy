package legacy.base_classes;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.LegacyCard;
import legacy.characters.TheAdventurer;

public class LevelUpChoiceCard extends LegacyCard {

  private final String classID;

  public LevelUpChoiceCard(String blightID, String name) {
    super(blightID + "_choice", name, "legacy/images/cards/class_level_up_choices/" + name.toLowerCase() + ".png",
            -2, "Gain 1 level of " + name + ".", CardType.POWER, CardRarity.SPECIAL, CardTarget.NONE);
    this.classID = blightID;
  }

  @Override
  public void upgrade() {
    // These cards shouldn't get upgraded.
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    // These card shouldn't get used.
  }

  @Override
  public void onChoseThisOption() {
    if (!(AbstractDungeon.player instanceof TheAdventurer)) return;

    ((TheAdventurer) AbstractDungeon.player).levelUp(this.classID);
  }

}
