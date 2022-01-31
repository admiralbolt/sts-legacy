package legacy.cards;

import basemod.abstracts.CustomCard;
import legacy.characters.TheAdventurer;

public abstract class LegacyCard extends CustomCard {

  public LegacyCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardRarity rarity, CardTarget target) {
    super(id, name, img, cost, rawDescription, type, TheAdventurer.Enums.COLOR_GRAY, rarity, target);
  }

}
