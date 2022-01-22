package legacy.cards.armor;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.LegacyCard;
import legacy.enchantments.Enchantment;

/**
 * Armor of Legacy.
 *
 * These are skills that can be **permanently** upgraded.
 */
public class LegacyArmor extends LegacyCard {

  public LegacyArmor(String id, CardStrings cardStrings, int cost, CardRarity rarity, CardTarget target) {
    super(id, cardStrings, cost, CardType.SKILL, rarity, target);
  }

  public void upgradeStats() {
    this.upgradeBlock(1);
    this.upgraded = true;
    ++this.timesUpgraded;
    this.name = this.getFormattedName();
    this.initializeTitle();
  }

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
