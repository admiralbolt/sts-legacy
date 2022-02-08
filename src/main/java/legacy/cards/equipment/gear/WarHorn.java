package legacy.cards.equipment.gear;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import legacy.LegacyMod;
import legacy.cards.LegacyCard;
import legacy.cards.LegacyCards;
import legacy.powers.GainTemporaryStrengthNextTurnPower;

/**
 * Brrrrrrr.
 *
 * Draw a card and gain 1 temporary Strength.
 * Next turn, draw a card and gain 1 temporary Strength.
 * Upgraded effect: Next turn draw 2 cards and gain 2 temporary Strength.
 */
public class WarHorn extends LegacyCard {

  public static final String ID = "legacy:war_horn";
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 1;

  public WarHorn() {
    super(ID, cardStrings.NAME, LegacyCards.getImagePath("gear", ID), COST,
            cardStrings.DESCRIPTION, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

    this.baseMagicNumber = this.magicNumber = 1;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeMagicNumber(1);
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.ORANGE, ShockWaveEffect.ShockWaveType.CHAOTIC), 1.0F));
    this.addToBot(new DrawCardAction(p, 1));
    this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 1), 1));
    this.addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, 1), 1));

    this.addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, this.magicNumber)));
    this.addToBot(new ApplyPowerAction(p, p, new GainTemporaryStrengthNextTurnPower(p, this.magicNumber)));
  }
}
