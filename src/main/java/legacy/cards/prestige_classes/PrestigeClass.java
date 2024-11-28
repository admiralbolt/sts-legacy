package legacy.cards.prestige_classes;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import legacy.LegacyMod;
import legacy.cards.LegacyCard;
import legacy.characters.TheAdventurer;

import java.util.ArrayList;

/**
 * Power cards that grant powerful buffs but require certain amounts of strength, dexterity, and focus to play.
 * Only 1 Prestige Class can be active at a time. Make sure that the prestige class power is the same name
 * as the card, but suffixed with '_power'.
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

  public PrestigeClass(String id, int cost, CardRarity rarity, StatRequirements statRequirements) {
    super(id, CardCrawlGame.languagePack.getCardStrings(id).NAME, cost, CardCrawlGame.languagePack.getCardStrings(id).DESCRIPTION, LegacyCardType.PRESTIGE_CLASS, CardType.POWER, rarity, CardTarget.SELF, statRequirements);
  }

  @Override
  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    if (!super.canUse(p, m)) return false;

    // Besides the stat requirements, we should also make sure they don't already have an instance of the prestige
    // class power they are trying to play.
    return !p.hasPower(this.cardID + "_power");
  }
}
