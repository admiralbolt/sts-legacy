package legacy.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.LegacyMod;
import legacy.characters.TheDefault;
import legacy.db.LegacyDb;
import legacy.enchantments.Enchantment;

import java.util.List;

/**
 * Default base case to abstract some things...
 */
public class BaseLegacyCard extends CustomCard {

  private List<Enchantment> enchantments;

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

    this.enchantments = LegacyMod.LEGACY_DB.loadCardEnchantments(id);
  }

  // This should be overriden
  @Override
  public void upgrade() {}

  /**
   * Applies effects from all enchantments.
   */
  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    for (Enchantment enchantment : this.enchantments) {
      enchantment.apply(p, m);
    }
  }
}
