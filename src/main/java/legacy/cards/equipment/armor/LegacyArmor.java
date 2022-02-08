package legacy.cards.equipment.armor;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import legacy.LegacyMod;
import legacy.cards.LegacyCard;
import legacy.cards.mods.enchantments.Enchantment;
import legacy.db.DBCardInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Armor of Legacy.
 *
 * These are skills that can be **permanently** upgraded.
 */
public abstract class LegacyArmor extends LegacyCard implements SpawnModificationCard {

  public static String getImagePath(String id) {
    return LegacyMod.MOD_ID + "/images/cards/armor/" + LegacyMod.getNameFromId(id) + ".png";
  }

  public List<Enchantment> enchantments;
  public final CardStrings cardStrings;

  public LegacyArmor(String id, CardStrings cardStrings, int cost, CardRarity rarity, CardTarget target) {
    super(id, LegacyMod.LEGACY_DB.getName(id, cardStrings.NAME), getImagePath(id), cost,
            cardStrings.DESCRIPTION, CardType.SKILL, rarity, target);

    this.cardStrings = cardStrings;

    // Properly load permanent upgrades / name from the db.
    DBCardInfo info = LegacyMod.LEGACY_DB.getCardInfo(id);
    this.baseBlock = this.block = info.value;

    this.baseMagicNumber = this.magicNumber = 1;
    // Enchantments need to get loaded from the DB, and their modifiers need to be applied.
    this.enchantments = LegacyMod.LEGACY_DB.loadCardEnchantments(id);
    for (Enchantment enchantment : this.enchantments) {
      CardModifierManager.addModifier(this, enchantment);
    }
  }

  // Armor cards are unique! If you already have a copy in your deck, you can't get more.
  @Override
  public boolean canSpawn(ArrayList<AbstractCard> currentRewardCards) {
    for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
      if (card.cardID.equals(this.cardID)) return false;
    }

    return true;
  }

}
