package legacy.cards.mods.enchantments;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.*;

/**
 * Weighted values for rolling random enchantments.
 *
 * Each enchantment has a base weight and a rarity multiplier. Rare enchantments have lower base weights, but higher
 * rarity multipliers to make them more likely for rarer cards.
 *
 * There's probably a class that does this already, but implementing this seems like a fun exercise.
 */
public class EnchantmentPool {

  private List<Enchantment> enchantments;
  private final Random random;

  public EnchantmentPool() {
    this.enchantments = new ArrayList<>();
    this.random = new Random();
  }

  public void addEnchantment(Enchantment enchantment) {
    this.enchantments.add(enchantment);
  }

  public List<Enchantment> rollEnchantments(AbstractCard card, int amount) {
    List<Enchantment> rolledEnchantments = new ArrayList<>();
    for (int i = 0; i < amount; ++i) {
      rolledEnchantments.add(rollEnchantment(card, rolledEnchantments));
    }
    return rolledEnchantments;
  }

  public Enchantment rollEnchantment(AbstractCard card, List<Enchantment> alreadyRolled) {
    int rarityMultiplier = 0;
    if (card.rarity == AbstractCard.CardRarity.RARE) {
      rarityMultiplier = 2;
    } else if (card.rarity == AbstractCard.CardRarity.UNCOMMON) {
      rarityMultiplier = 1;
    }

    // Calculate the splits for rolling for an enchantment. These are the points at which we should get a given
    // enchantment. For example, if we have splits [8, 11, 16] this means we get enchantment #0 if we roll a 0-7,
    // enchantment #1 if we roll an 8-10, and enchantment #2 if we roll an 11-15.
    List<Integer> splits = new ArrayList<>();
    List<Enchantment> potentialEnchantments = new ArrayList<>();
    int totalWeight = 0;
    for (Enchantment enchantment : this.enchantments) {
      // Make sure our card doesn't already have this enchantment.
      if (CardModifierManager.hasModifier(card, enchantment.id)) continue;

      // Make sure we haven't already rolled this enchantment.
      if (alreadyRolled.contains(enchantment)) continue;

      totalWeight += enchantment.weight + enchantment.rarityMultiplier * rarityMultiplier;
      splits.add(totalWeight);
      potentialEnchantments.add(enchantment);
    }

    // Special handling for Bane enchantment. We don't want to add 6 copies of it to the pool, instead we add one
    // copy with a random monster type.
    if (!CardModifierManager.hasModifier(card, Bane.ID)) {
      // Roll a random bane enchantment.
      Enchantment enchantment = new Bane();
      totalWeight += enchantment.weight + enchantment.rarityMultiplier * rarityMultiplier;
      splits.add(totalWeight);
      potentialEnchantments.add(enchantment);
    }

    int roll = this.random.nextInt(totalWeight);

    // Bisect the splits, and find our enchantment.
    return potentialEnchantments.get(bisect(splits, roll, 0, splits.size()));
  }

  private int bisect(List<Integer> splits, int roll, int left, int right) {
    if (left == right) return left;

    int middle = (left + right) / 2;
    int splitsValue = splits.get(middle);
    // We miraculously found the answer!
    if (splitsValue == roll) return middle;

    // Answer is in the right half.
    if (roll > splitsValue) return bisect(splits, roll, middle + 1, right);

    // Answer is in the left half!
    return bisect(splits, roll, left, middle);
  }

}
