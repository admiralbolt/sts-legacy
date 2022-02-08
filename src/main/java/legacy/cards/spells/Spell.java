package legacy.cards.spells;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.NoBlockPower;
import com.megacrit.cardcrawl.stances.WrathStance;
import legacy.cards.LegacyCard;
import legacy.cards.LegacyCards;
import legacy.cards.mods.SpellModifier;
import legacy.characters.TheAdventurer;

import java.util.ArrayList;

/**
 * Magic is real btw.
 */
public abstract class Spell extends LegacyCard implements SpawnModificationCard {

  public enum SpellSchool {
    ABJURATION,
    CONJURATION,
    DIVINATION,
    ENCHANTMENT,
    EVOCATION,
    ILLUSION,
    NECROMANCY,
    TRANSMUTATION
  }

  public final int focusRequirement;
  public final SpellSchool school;

  // Construct a description string with the proper stats requirements.
  // i.e. "Requires 2 Focus NL "
  // This is static because it gets prepended in the constructor.
  public static String requirementsString(int focusRequirement) {
    return (focusRequirement > 0) ? "Requires " + focusRequirement + " Focus. NL " : "";
  }

  public Spell(String id, CardStrings cardStrings, int cost, CardType type, CardRarity rarity, CardTarget target, SpellSchool school, int focusRequirement) {
    super(id, cardStrings.NAME, LegacyCards.getImagePath("spells", id), cost,
            requirementsString(focusRequirement) + cardStrings.DESCRIPTION, type, rarity, target);

    CardModifierManager.addModifier(this, new SpellModifier());
    this.focusRequirement = focusRequirement;
    this.school = school;
  }

  // Spells have a required amount of focus in order to play them. Also, you can't cast spells while in wrath, duh.
  @Override
  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    if (!super.canUse(p, m)) return false;
    if (p.stance.ID.equals(WrathStance.STANCE_ID)) return false;

    AbstractPower focus = p.getPower(FocusPower.POWER_ID);
    int focusAmount = (focus == null) ? 0 : focus.amount;
    return focusAmount >= this.focusRequirement;
  }

  // We only want to spawn spell cards if the player can play them.
  // In this case we set the needed wizardLevels to one less than the required focus.
  @Override
  public boolean canSpawn(ArrayList<AbstractCard> currentRewardCards) {
    if (!(AbstractDungeon.player instanceof TheAdventurer)) return false;

    TheAdventurer adventurer = (TheAdventurer) AbstractDungeon.player;
    return adventurer.wizardLevel >= (this.focusRequirement - 1);
  }

  // Spells should scale with focus instead of Strength or Dexterity.
  // Additionally, spells will ignore bonus damage from physical effects. Being weakened shouldn't affect your fireball,
  // and the enemy being vulnerable shouldn't make your acid arrow deal more damage.
  // In order for this to work as intended I need to do a full override of the applyPowers() and applyPowersToBlock()
  // methods. Digging into the applyPowers() function:
  //
  // 1. Call applyPowersToBlock(), see below.
  // 2. Loop through the player relics and call atDamageModify(). This is only StrikeDummy & WristBlade so we can safely
  //    ignore this.
  // 3. Loop through the player powers and call atDamageGive(). This is for PenNibPower, StrengthPower, VigorPower, and
  //    WeakPower. We can ignore these as well.
  // 4. Apply stance atDamageGive(). Wrath / Divinity damage shouldn't apply here, so we can skip this.
  // 5. Loop through the player powers and call atDamageFinalGive(). Seems like nothing uses this?
  // 6. Set this.damage to the new val, and set this.isDamageModified.
  @Override
  public void applyPowers() {
    this.applyPowersToBlock();

    AbstractPower focus = AbstractDungeon.player.getPower(FocusPower.POWER_ID);
    int focusAmount = (focus == null) ? 0 : focus.amount;
    if (focusAmount == 0) return;

    // Modify damage based on focus.
    this.addDamage(focusAmount);
    this.isDamageModified = true;
  }

  // Digging into the applyPowersToBlock() function:
  //
  // 1. Loop through the player powers and call modifyBlock(). This is for DexterityPower and FrailPower, don't care.
  // 2. Loop through the player powers and call modifyBlockLast(). This is for the NoBlockPower. We _DO_ care about
  //    this power. PanicButton should still stop you from gaining block.
  // 3. Set this.block to the new val, and set this.isBlockModified.
  @Override
  protected void applyPowersToBlock() {
    AbstractPower focus = AbstractDungeon.player.getPower(FocusPower.POWER_ID);
    int focusAmount = (focus == null) ? 0 : focus.amount;
    if (focusAmount == 0) return;

    // Modify block based on focus.
    this.block = this.baseBlock + focusAmount;

    // Set block to 0 if we have the NoBlockPower.
    if (AbstractDungeon.player.hasPower(NoBlockPower.POWER_ID)) this.block = 0;

    this.isBlockModified = true;
  }

  private void addDamage(int amount) {
    if (this.isMultiDamage && this.multiDamage != null) {
      for (int i = 0; i < this.multiDamage.length; ++i) {
        this.multiDamage[i] += amount;
      }
    }
    this.damage += amount;
  }

  // For our purposes, identical to applyPowers(). We don't care about Vulnerable or Slow.
  @Override
  public void calculateCardDamage(AbstractMonster mo) {
    AbstractPower focus = AbstractDungeon.player.getPower(FocusPower.POWER_ID);
    int focusAmount = (focus == null) ? 0 : focus.amount;
    if (focusAmount == 0) return;

    // Modify damage based on focus.
    this.addDamage(focusAmount);
  }
}
