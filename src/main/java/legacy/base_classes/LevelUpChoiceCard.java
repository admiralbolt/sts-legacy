package legacy.base_classes;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.LegacyCard;

public class LevelUpChoiceCard extends LegacyCard {

  public LevelUpChoiceCard(String blightID, String name) {
    super(blightID + "_choice", name, "legacy/images/cards/class_level_up_choices/" + name.toLowerCase() + ".png",
            -2, "Gain 1 level of " + name + ".", CardType.POWER, CardRarity.SPECIAL, CardTarget.NONE);
  }

  @Override
  public void upgrade() {
    // These cards shouldn't get upgraded.
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    // These card shouldn't get used.
  }

}
