package legacy.cards.prestige_classes;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import legacy.LegacyMod;
import legacy.cards.LegacyCard;
import legacy.characters.TheAdventurer;

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

  public PrestigeClass(String id, CardStrings cardStrings, int cost, CardRarity rarity, int strengthRequirement, int dexterityRequirement, int focusRequirement) {
    super(id, cardStrings.NAME, getImagePath(id), cost, cardStrings.DESCRIPTION, CardType.POWER,
            TheAdventurer.Enums.COLOR_GRAY, rarity, CardTarget.SELF);

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
