package legacy.cards.equipment.armor;

import com.megacrit.cardcrawl.localization.CardStrings;
import legacy.cards.equipment.EquipmentCard;

/**
 * Armor of Legacy.
 *
 * These are skills that can be **permanently** upgraded.
 */
public class LegacyArmor extends EquipmentCard {

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

}
