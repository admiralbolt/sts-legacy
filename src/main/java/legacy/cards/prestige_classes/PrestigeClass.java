package legacy.cards.prestige_classes;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import legacy.LegacyMod;
import legacy.cards.LegacyCard;

/**
 * Power cards that grant powerful buffs but require certain amounts of strength, dexterity, and focus to play.
 * Only 1 Prestige Class can be active at a time.
 *
 * Each of the base classes corresponds to one of these powers ->
 *   Fighter - Strength
 *   Rogue - Dexterity
 *   Wizard - Focus
 *
 * Rather than depending on class levels though, by depending on the raw powers, it will interact nicely with other
 * cards / relics.
 */
public abstract class PrestigeClass extends LegacyCard {

  public int strengthRequirement = 0;
  public int dexterityRequirement = 0;
  public int focusRequirement = 0;

  public boolean needsStrength;
  public boolean needsDexterity;
  public boolean needsFocus;

  public static String getImagePath(String id) {
    return LegacyMod.getModID() + "/images/cards/prestige_classes/" + LegacyMod.getNameFromId(id) + ".png";
  }

  // Construct a description string with the proper stats requirements.
  // i.e. "Requires 2 Strength NL Requires 2 Dexterity NL"
  // This is static because it gets prepended in the constructor.
  public static String requirementsString(int strengthRequirement, int dexterityRequirement, int focusRequirement) {
    StringBuilder builder = new StringBuilder();
    if (strengthRequirement > 0) {
      builder.append("Requires ");
      builder.append(strengthRequirement);
      builder.append(" Strength. NL ");
    }
    if (dexterityRequirement > 0) {
      builder.append("Requires ");
      builder.append(dexterityRequirement);
      builder.append(" Dexterity. NL ");
    }
    if (focusRequirement > 0) {
      builder.append("Requires ");
      builder.append(focusRequirement);
      builder.append(" Focus. NL ");
    }
    return builder.toString();
  }

  public PrestigeClass(String id, CardStrings cardStrings, int cost, CardRarity rarity, int strengthRequirement, int dexterityRequirement, int focusRequirement) {
    super(id, cardStrings.NAME, getImagePath(id), cost,
            requirementsString(strengthRequirement, dexterityRequirement, focusRequirement) + cardStrings.DESCRIPTION,
            CardType.POWER, rarity, CardTarget.SELF);

    this.strengthRequirement = strengthRequirement;
    this.needsStrength = strengthRequirement > 0;

    this.dexterityRequirement = dexterityRequirement;
    this.needsDexterity = dexterityRequirement > 0;

    this.focusRequirement = focusRequirement;
    this.needsFocus = focusRequirement > 0;
  }

  @Override
  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    boolean canUse = super.canUse(p, m);
    if (!canUse) return false;

    if (this.needsStrength) {
      AbstractPower strength = p.getPower("Strength");
      int strengthAmount = (strength == null) ? 0 : strength.amount;
      if (strengthAmount < strengthRequirement) return false;
    }

    if (this.needsDexterity) {
      AbstractPower dexterity = p.getPower("Dexterity");
      int dexterityAmount = (dexterity == null) ? 0 : dexterity.amount;
      if (dexterityAmount < dexterityRequirement) return false;
    }

    if (this.needsFocus) {
      AbstractPower focus = p.getPower("Focus");
      int focusAmount = (focus == null) ? 0 : focus.amount;
      if (focusAmount < focusRequirement) return false;
    }

    return true;
  }

}
