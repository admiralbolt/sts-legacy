package legacy.cards;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import legacy.LegacyMod;
import legacy.cards.mods.enchantments.Enchantment;
import legacy.cards.mods.enchantments.EnchantmentUtils;
import legacy.characters.TheAdventurer;
import legacy.patches.LegacyCardTags;

import java.util.ArrayList;

/**
 * Base class for all my new cards.
 */
public abstract class LegacyCard extends CustomCard implements SpawnModificationCard {

  public static class StatRequirements {
    public int strength = 0;
    public int dexterity = 0;
    public int focus = 0;

    public StatRequirements(int strength, int dexterity, int focus) {
      this.strength = strength;
      this.dexterity = dexterity;
      this.focus = focus;
    }

    // Construct a description string with the proper stats requirements.
    // i.e. "Requires 2 Strength NL Requires 2 Dexterity NL"
    // This is static because it gets prepended in the constructor.
    public String requirementsString() {
      StringBuilder builder = new StringBuilder();
      if (this.strength > 0) {
        builder.append("Requires ").append(this.strength).append(" Strength. NL ");
      }
      if (this.dexterity > 0) {
        builder.append("Requires ").append(this.dexterity).append(" Dexterity. NL ");
      }
      if (this.focus > 0) {
        builder.append("Requires ").append(this.focus).append(" Focus. NL ");
      }
      return builder.toString();
    }
  }

  // Second magic number used for some stuff.
  public int magicNumberTwo;
  public int baseMagicNumberTwo;
  public boolean upgradedMagicNumberTwo;
  public boolean isMagicNumberTwoModified;
  public StatRequirements statRequirements;

  public LegacyCardType legacyCardType;
  public boolean enchantable = false;
  public CardStrings cardStrings;

  public enum LegacyCardType {
    WEAPON,
    ARMOR,
    GEAR,
    SPELL,
    PRESTIGE_CLASS,
    MISC
  }

  // This constructor is used for non playable card options, like level ups & enchantment choices.
  public LegacyCard(String id, String name, String image, int cost, String rawDescription, CardType type, CardRarity rarity, CardTarget target) {
    super(id, name, image, cost, rawDescription, type, TheAdventurer.Enums.COLOR_GRAY, rarity, target);
  }

  public LegacyCard(String id, int cost, LegacyCardType legacyCardType, CardType type, CardRarity rarity, CardTarget target) {
    this(id, cost, legacyCardType, type, rarity, target, new StatRequirements(0, 0, 0));

    this.legacyCardType = legacyCardType;
  }

  public LegacyCard(String id, int cost, LegacyCardType legacyCardType, CardType type, CardRarity rarity, CardTarget target, StatRequirements statRequirements) {
    super(id, CardCrawlGame.languagePack.getCardStrings(id).NAME, getImagePath(legacyCardType, id), cost, ((legacyCardType == LegacyCardType.PRESTIGE_CLASS) ? "legacy:Prestige_Class NL " : "") + statRequirements.requirementsString() + CardCrawlGame.languagePack.getCardStrings(id).DESCRIPTION, type, TheAdventurer.Enums.COLOR_GRAY, rarity, target);

    this.cardStrings = CardCrawlGame.languagePack.getCardStrings(id);
    this.statRequirements = statRequirements;
    this.legacyCardType = legacyCardType;
  }

  protected void upgradeMagicNumberTwo(int amount) {
    this.baseMagicNumberTwo += amount;
    this.magicNumberTwo = this.baseMagicNumberTwo;
    this.upgradedMagicNumberTwo = true;
  }

  // Updates the card name based on current enchantments.
  public void updateName() {
    StringBuilder builder = new StringBuilder();
    for (Enchantment enchantment : EnchantmentUtils.getCardEnchantments(this)) {
      builder.append(enchantment.name);
      builder.append(" ");
    }

    builder.append(this.originalName);
    this.name = builder.toString();
    this.initializeTitle();
  }

  public static String getImagePath(LegacyCardType type, String id) {
    return LegacyMod.MOD_ID + "/images/cards/" + type.toString().toLowerCase() + "/" + LegacyMod.getNameFromId(id) + ".png";
  }

  @Override
  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    if (!super.canUse(p, m)) return false;

    if (this.statRequirements.strength > 0) {
      AbstractPower strength = p.getPower("Strength");
      int strengthAmount = (strength == null) ? 0 : strength.amount;
      if (strengthAmount < this.statRequirements.strength) return false;
    }

    if (this.statRequirements.dexterity > 0) {
      AbstractPower dexterity = p.getPower("Dexterity");
      int dexterityAmount = (dexterity == null) ? 0 : dexterity.amount;
      if (dexterityAmount < this.statRequirements.dexterity) return false;
    }

    if (this.statRequirements.focus > 0) {
      AbstractPower focus = p.getPower("Focus");
      int focusAmount = (focus == null) ? 0 : focus.amount;
      if (focusAmount < this.statRequirements.focus) return false;
    }

    return true;
  }

  // We only want to spawn cards *IF* the player has the required stats to play them.
  // We set the required class levels at one less than the stat requirements.
  @Override
  public boolean canSpawn(ArrayList<AbstractCard> currentRewardCards) {
    if (!(AbstractDungeon.player instanceof TheAdventurer)) return false;

    TheAdventurer adventurer = (TheAdventurer) AbstractDungeon.player;
    if (adventurer.fighterLevel < (this.statRequirements.strength - 1)) return false;
    if (adventurer.rogueLevel < (this.statRequirements.dexterity - 1)) return false;
    if (adventurer.wizardLevel < (this.statRequirements.focus - 1)) return false;

    // Additionally, we want to check if it's a card that depends on stealth.
    // We don't want to offer any cards that require stealth until a player
    // can actually enter it.
    if (this.hasTag(LegacyCardTags.REQUIRES_STEALTH)) {
      for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
        if (card.hasTag(LegacyCardTags.ENTERS_STEALTH)) return true;
      }
      return false;
    }

    return true;
  }


}
