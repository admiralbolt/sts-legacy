package legacy.cards.equipment.gear;

import basemod.cardmods.EtherealMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.RemoveAllPowersAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import legacy.actions.BetterRemoveAllPowersAction;
import legacy.cards.LegacyCard;
import legacy.cards.LegacyCards;

/**
 * The most powerful piece of equipment in all of the game.
 *
 * Remove all buffs from an enemy, gain 1 intagible.
 */
public class TenFootPole extends LegacyCard {

  public static final String ID = "legacy:ten_foot_pole";
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 3;

  public TenFootPole() {
    super(ID, cardStrings.NAME, LegacyCards.getImagePath("gear", ID), COST,
            cardStrings.DESCRIPTION, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);

    this.baseMagicNumber = this.magicNumber = 1;
    this.exhaust = true;
    CardModifierManager.addModifier(this, new EtherealMod());
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    CardModifierManager.removeModifiersById(this, EtherealMod.ID, true);
    this.upgradeName();
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new BetterRemoveAllPowersAction(m, AbstractPower.PowerType.BUFF));
    this.addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1), 1));
  }

}
