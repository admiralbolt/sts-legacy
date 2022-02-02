package legacy.cards;

import basemod.abstracts.CustomCard;
import legacy.characters.TheAdventurer;

/**
 * Base class for all my new cards.
 */
public abstract class LegacyCard extends CustomCard {

  public LegacyCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardRarity rarity, CardTarget target) {
    super(id, name, img, cost, rawDescription, type, TheAdventurer.Enums.COLOR_GRAY, rarity, target);
  }

}
