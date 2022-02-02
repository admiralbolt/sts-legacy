package legacy.base_classes;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.LegacyCard;

public abstract class LevelUpChoiceCard extends LegacyCard {

  public LevelUpChoiceCard(String id, CardStrings cardStrings, String imgName) {
    super(id, cardStrings.NAME, "legacy/images/cards/class_level_up_choices/" + imgName,
            -2, cardStrings.DESCRIPTION, CardType.POWER, CardRarity.SPECIAL, CardTarget.NONE);
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
