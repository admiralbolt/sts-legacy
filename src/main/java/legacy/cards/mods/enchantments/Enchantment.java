package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import legacy.cards.LegacyCard;
import legacy.cards.equipment.armor.LegacyArmor;
import legacy.cards.equipment.weapons.LegacyWeapon;

/**
 * Enchantments are a special type of modifier that gets applied only to weapons & armor.
 */
public abstract class Enchantment extends AbstractCardModifier {

  public String id;
  public String name;
  public String description;
  // Which equipment type this enchantment can be applied to.
  public LegacyCard.LegacyCardType type;
  // The "rarity" of the enchantment. Used for border on select screens.
  public AbstractCard.CardRarity rarity;
  // The default weight for rolling the enchantment.
  public int weight;
  // The rarity of the enchanted card affects the quality of enchantments.
  public int rarityMultiplier;

  public Enchantment(String id, String name, String description, LegacyCard.LegacyCardType type, AbstractCard.CardRarity enchantmentRarity, int weight, int rarityMultiplier) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.type = type;
    this.rarity = enchantmentRarity;
    this.weight = weight;
    this.rarityMultiplier = rarityMultiplier;
  }

  @Override
  public String modifyDescription(String rawDescription, AbstractCard card) {
    return rawDescription + " NL " + this.description;
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return null;
  }

  @Override
  public String identifier(AbstractCard card) {
    return this.id;
  }

  // Enchantment color is used to modify the border glow.
  public Color getColor() {
    return null;
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  @Override
  public boolean shouldApply(AbstractCard card) {
    // Only apply an enchantment if the card doesn't already have it.
    return !CardModifierManager.hasModifier(card, this.id);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Enchantment)) return false;

    return this.id.equals(((Enchantment) obj).id);
  }

  public boolean canApply(AbstractCard card) {
    // Enchantments must match the type of the card.
    if (this.type == LegacyCard.LegacyCardType.ARMOR && !(card instanceof LegacyArmor)) return false;
    if (this.type == LegacyCard.LegacyCardType.WEAPON && !(card instanceof LegacyWeapon)) return false;

    // Enchantments on a card should be unique.
    if (CardModifierManager.hasModifier(card, this.id)) return false;

    return true;
  }

  // Tiny bit o' short hand.
  public void addToBot(AbstractGameAction action) {
    AbstractDungeon.actionManager.addToBottom(action);
  }

  @Override
  public void onInitialApplication(AbstractCard card) {
    if (card instanceof LegacyCard) ((LegacyCard) card).updateName();
    // Need to update the card in the card library.
    CardLibrary.add(card);
  }

}
