package legacy.cards.armor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import legacy.LegacyMod;
import legacy.characters.TheDefault;
import legacy.db.DBCardInfo;
import legacy.enchantments.Enchantment;
import legacy.util.CardUtils;

import java.util.List;

/**
 * Default base case to abstract some things...
 */
public class LegacyArmor extends CustomCard {

  private final List<Enchantment> enchantments;
  private final CardStrings cardStrings;

  public LegacyArmor(String id, CardStrings cardStrings, int cost, CardRarity rarity, CardTarget target) {
    super(
            id,
            LegacyMod.LEGACY_DB.getName(id, cardStrings.NAME),
            LegacyMod.makeCardPathFromId(id),
            cost,
            LegacyMod.LEGACY_DB.getCardDescription(id, cardStrings.DESCRIPTION),
            CardType.SKILL,
            TheDefault.Enums.COLOR_GRAY,
            rarity,
            target
    );

    DBCardInfo info = LegacyMod.LEGACY_DB.getCardInfo(id);
    if (info.numUpgrades > 0) {
      this.timesUpgraded = info.numUpgrades;
      this.upgraded = true;
      this.initializeTitle();
    }

    this.baseBlock = this.block = info.value;
    this.baseMagicNumber = this.magicNumber = 1;
    this.cardStrings = cardStrings;
    this.enchantments = LegacyMod.LEGACY_DB.loadCardEnchantments(id);
  }

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
    this.upgradeBlock(1);
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
   */
  @Override
  public void upgrade() {
    this.upgradeStats();

    // This is a terrible hack -- when we upgrade a card we check to make sure it's actually in our deck.
    if (!CardUtils.isCardInstanceInDeck(this.uuid)) return;

    // Also, if we have temporary upgrade effects they should also only apply to a single instance of the card.
    // This will mean that Lesson Learned won't work until this is handled properly.
    if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom) return;

    // If we get here, we need to update ALL other instances of this card in our deck,
    // and persist changes to the DB.
    for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
      if (card.uuid.equals(this.uuid)) continue;
      if (!card.cardID.equals(this.cardID)) continue;
      if (!(card instanceof LegacyArmor)) continue;

      ((LegacyArmor) card).upgradeStats();
    }

    LegacyMod.LEGACY_DB.upgradeCard(this.cardID);
  }

  @Override
  public boolean canUpgrade() {
    return true;
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
