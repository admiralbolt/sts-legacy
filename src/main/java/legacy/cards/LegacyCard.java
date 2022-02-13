package legacy.cards;

import basemod.abstracts.CustomCard;
import legacy.characters.TheAdventurer;

/**
 * Base class for all my new cards.
 */
public abstract class LegacyCard extends CustomCard {

  // Second magic number used for some stuff.
  public int magicNumberTwo;
  public int baseMagicNumberTwo;
  public boolean upgradedMagicNumberTwo;
  public boolean isMagicNumberTwoModified;

  public LegacyCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardRarity rarity, CardTarget target) {
    super(id, name, img, cost, rawDescription, type, TheAdventurer.Enums.COLOR_GRAY, rarity, target);
  }

  protected void upgradeMagicNumberTwo(int amount) {
    this.baseMagicNumberTwo += amount;
    this.magicNumberTwo = this.baseMagicNumberTwo;
    this.upgradedMagicNumberTwo = true;
  }

}
