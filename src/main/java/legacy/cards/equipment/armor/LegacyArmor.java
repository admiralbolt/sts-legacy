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
import legacy.cards.mods.enchantments.Enchantment;
import legacy.cards.mods.traits.HeavyArmorTrait;
import legacy.cards.mods.traits.MediumArmorTrait;

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

  public LegacyArmor(String id, int cost, CardRarity rarity, AbstractCardModifier ...modifiers) {
    super(id, cost, LegacyCardType.ARMOR, CardType.SKILL, rarity, CardTarget.SELF);

    this.enchantable = true;

    this.baseMagicNumber = this.magicNumber = 1;

    // All the armor traits should be applied.
    for (AbstractCardModifier modifier : modifiers) {
      CardModifierManager.addModifier(this, modifier);
    }

    this.updateName();
  }

  // Armor cards are unique! If you already have a copy in your deck, you can't get more.
  @Override
  public boolean canSpawn(ArrayList<AbstractCard> currentRewardCards) {
    return AbstractDungeon.player.masterDeck.group.stream().noneMatch((card) -> card.cardID.equals(this.cardID));
  }

  @Override
  public void applyPowersToBlock() {
    super.applyPowersToBlock();

    // Handle our armor traits here, specifically for heavy / medium armor.
    AbstractPower dexterity = AbstractDungeon.player.getPower("Dexterity");
    if (dexterity == null || dexterity.amount == 0) return;

    if (CardModifierManager.hasModifier(this, HeavyArmorTrait.ID)) {
      this.block = this.baseBlock;
      this.isBlockModified = false;
    } else if (CardModifierManager.hasModifier(this, MediumArmorTrait.ID) && dexterity.amount > 3) {
      this.block -= (dexterity.amount - 3);
      this.isBlockModified = true;
    }
  }

}
