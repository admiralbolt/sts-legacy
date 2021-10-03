package legacy.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.localization.CardStrings;
import legacy.LegacyMod;
import legacy.characters.TheDefault;

/**
 * Default base case to abstract some things...
 */
public abstract class BaseLegacyCard extends CustomCard {

  public BaseLegacyCard(String id, CardStrings cardStrings, int cost, CardType type, CardRarity rarity, CardTarget target) {
    super(
            id,
            LegacyMod.LEGACY_DB.getName(id, cardStrings.NAME),
            LegacyMod.makeCardPathFromId(id),
            cost,
            cardStrings.DESCRIPTION,
            type,
            TheDefault.Enums.COLOR_GRAY,
            rarity,
            target
    );
  }
}
