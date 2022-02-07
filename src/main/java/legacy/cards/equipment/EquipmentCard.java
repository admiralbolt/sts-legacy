package legacy.cards.equipment;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.LegacyMod;
import legacy.cards.LegacyCard;
import legacy.cards.mods.enchantments.Enchantment;
import legacy.db.DBCardInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Equipment Cards!
 *
 * These are cards that can be permanently modified via enchanting.
 * Enchantments per card are stored in an sqlite database. See LegacyDb for more info.
 */
public abstract class EquipmentCard extends LegacyCard implements SpawnModificationCard {

  public final List<Enchantment> enchantments;
  public final CardStrings cardStrings;

  public EquipmentCard(String id, CardStrings cardStrings, String imagePath, int cost, CardType cardType, CardRarity rarity, CardTarget target) {
    super(id, LegacyMod.LEGACY_DB.getName(id, cardStrings.NAME), imagePath, cost,
            cardStrings.DESCRIPTION, cardType, rarity, target);

    // Properly load permanent upgrades / name from the db.
    DBCardInfo info = LegacyMod.LEGACY_DB.getCardInfo(id);

    if (cardType == CardType.ATTACK) {
      this.baseDamage = info.value;
    } else if (cardType == CardType.SKILL) {
      this.baseBlock = this.block = info.value;
    }

    this.baseMagicNumber = this.magicNumber = 1;
    this.cardStrings = cardStrings;
    this.enchantments = LegacyMod.LEGACY_DB.loadCardEnchantments(id);
    // We need to add the enchantment modifier to the card on load as well.
    for (Enchantment enchantment : this.enchantments) {
      CardModifierManager.addModifier(this, enchantment);
    }
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

  }

  // Equipment cards are unique! If you already have a copy in your deck, you can't get more.
  @Override
  public boolean canSpawn(ArrayList<AbstractCard> currentRewardCards) {
    System.out.println("canSpawn triggering: " + this.cardID);
    for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
      if (card.cardID.equals(this.cardID)) {
        System.out.println("Not spawning card: " + this.cardID);
        return false;
      }
    }

    return true;
  }

}
