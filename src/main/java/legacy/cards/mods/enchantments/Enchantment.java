package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import legacy.cards.LegacyCard;

/**
 * Enchantments are a special type of modifier that gets applied only to weapons & armor.
 */
public abstract class Enchantment extends AbstractCardModifier {

  public String id;
  public String name;
  public String description;
  // Which equipment type this enchantment can be applied to.
  public LegacyCard.LegacyCardType type;
  // The default weight for rolling the enchantment.
  public int weight;
  // The rarity of the enchanted card affects the quality of enchantments.
  public int rarityMultiplier;

  public Enchantment(String id, String name, String description, LegacyCard.LegacyCardType type, int weight, int rarityMultiplier) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.type = type;
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
}
