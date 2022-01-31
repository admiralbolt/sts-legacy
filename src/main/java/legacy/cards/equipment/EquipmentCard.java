package legacy.cards.equipment;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.LegacyMod;
import legacy.cards.LegacyCard;
import legacy.cards.mods.enchantments.Enchantment;
import legacy.db.DBCardInfo;
import legacy.util.CardUtils;

import java.util.List;

public class EquipmentCard extends LegacyCard {

  public final List<Enchantment> enchantments;
  public final CardStrings cardStrings;

  public EquipmentCard(String id, CardStrings cardStrings, String imagePath, int cost, CardType cardType, CardRarity rarity, CardTarget target) {
    super(
            id,
            LegacyMod.LEGACY_DB.getName(id, cardStrings.NAME),
            imagePath,
            cost,
            cardStrings.DESCRIPTION,
            cardType,
            rarity,
            target
    );

    // Properly load permanent upgrades /  name from the db.
    DBCardInfo info = LegacyMod.LEGACY_DB.getCardInfo(id);
    if (info.numUpgrades > 0) {
      this.timesUpgraded = info.numUpgrades;
      this.upgraded = true;
      this.initializeTitle();
    }

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

  /**
   * Returns a nicely formatted name based on upgrades & enchantments.
   *
   * Ex: +2 Corrosive Returning Greatsword
   */
  public String getFormattedName() {
    StringBuilder builder = new StringBuilder();
    if (this.timesUpgraded > 0) {
      builder.append("+");
      builder.append(this.timesUpgraded);
      builder.append(" ");
    }

    for (Enchantment enchantment : this.enchantments) {
      builder.append(enchantment.name);
      builder.append(" ");
    }

    builder.append(this.cardStrings.NAME);
    return builder.toString();
  }

  public void upgradeStats() {
    this.upgradeDamage(1);
    this.upgraded = true;
    ++this.timesUpgraded;
    this.name = this.getFormattedName();
    this.initializeTitle();
  }

  /**
   * When a legacy weapon gets upgraded, we want to permanently increase the damage by 1. This means:
   * 1. Updating the damage & name for all instances of the current card within the current run.
   * 2. Updating the database so that upgrades are properly loaded on subsequent runs.
   *
   * The way that upgrading actually works though, is that a duplicate card gets created with upgrade()
   * called on that. We need to ensure that when upgrade() is called on a card *actually* in our deck
   * that the database gets updated, but ONLY in that case AND we don't want to loop infinitely.
   *
   * Another note while I'm thinking about it -- It is always possible to save and reload after smithing at a campfire.
   * This means that all cards are infinitely upgradeable with some use of cheating. Should probably figure out a way
   * to resolve this.
   */
  @Override
  public void upgrade() {
    // A very annoying check we need to make -- when you "resume" a game after saving / quitting. This clears
    // the master deck, and then creates a copy of it calling upgrade() on each card. This means that everytime we
    // save and reload, we would re-permanently upgrade all cards. The relevant bit is in CardCrawlGame.java:
    //
    // p.masterDeck.clear();
    // for (CardSave s : saveFile.cards) {
    //   p.masterDeck.addToTop(CardLibrary.getCopy(s.id, s.upgrades, s.misc));
    // }
    //
    // In our particular case we don't need to run this upgrade() logic, because our DB persists that info for us.
    // That includes upgrading the local instance of the card.
    if (CardCrawlGame.loadingSave) return;

    this.upgradeStats();

    // Permanent upgrades affect cards in the master deck. Temporary upgrades only affect cards in hand.
    // This checks for upgrades that are affecting the master deck, since that's the only case where we want to
    // upgrade everything.
    if (!CardUtils.isCardInstanceInMasterDeck(this.uuid)) return;

    // If we get here, we need to update ALL other instances of this card in our deck,
    // and persist changes to the DB.
    for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
      if (card.uuid.equals(this.uuid)) continue;
      if (!card.cardID.equals(this.cardID)) continue;
      if (!(card instanceof EquipmentCard)) continue;

      ((EquipmentCard) card).upgradeStats();
    }

    LegacyMod.LEGACY_DB.upgradeCard(this.cardID);
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

  }

  @Override
  public boolean canUpgrade() {
    return true;
  }
}
