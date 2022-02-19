package legacy.cards.equipment.armor;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import legacy.LegacyMod;
import legacy.cards.LegacyCard;
import legacy.cards.mods.ModifierWithBadge;
import legacy.cards.mods.enchantments.Enchantment;
import legacy.cards.mods.traits.HeavyArmorTrait;
import legacy.cards.mods.traits.MediumArmorTrait;
import legacy.cards.mods.traits.TwoHandedTrait;
import legacy.db.DBCardInfo;

import java.lang.reflect.Modifier;
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

  public LegacyArmor(String id, CardStrings cardStrings, int cost, CardRarity rarity, AbstractCardModifier ...modifiers) {
    super(id, LegacyMod.LEGACY_DB.getName(id, cardStrings.NAME), getImagePath(id), cost,
            cardStrings.DESCRIPTION, CardType.SKILL, rarity, CardTarget.SELF);

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

    // All the armor traits should be applied.
    for (AbstractCardModifier modifier : modifiers) {
      CardModifierManager.addModifier(this, modifier);
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

  @Override
  public void applyPowers() {
    super.applyPowers();

    // Handle our armor traits here, specifically for heavy / medium armor.
    AbstractPower dexterity = AbstractDungeon.player.getPower("Dexterity");
    if (dexterity == null || dexterity.amount == 0) return;

    if (CardModifierManager.hasModifier(this, HeavyArmorTrait.ID)) {
      this.block -= dexterity.amount;
      if (this.block == this.baseBlock) this.isBlockModified = false;
    } else if (CardModifierManager.hasModifier(this, MediumArmorTrait.ID) && dexterity.amount > 3) {
      this.block -= (dexterity.amount - 3);
      this.isBlockModified = true;
    }
  }

}
