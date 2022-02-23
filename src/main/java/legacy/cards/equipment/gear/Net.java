package legacy.cards.equipment.gear;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.EntangleEffect;
import legacy.cards.LegacyCard;

/**
 * Slayer of byrds.
 *
 * The idea is to make net like the Red Slaver's entangle.
 */
public class Net extends LegacyCard {

  public static final String ID = "legacy:net";
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 2;

  public Net() {
    super(ID, cardStrings.NAME, COST, cardStrings.DESCRIPTION, LegacyCardType.GEAR, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);

    this.baseMagicNumber = this.magicNumber = 2;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.target = CardTarget.ALL_ENEMY;
    this.upgradeName();
    this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (!this.upgraded) {
      throwNet(p, m);
      return;
    }

    // If upgraded, throw a net at every monster in combat.
    for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
      if (monster.isDeadOrEscaped()) continue;

      throwNet(p, monster);
    }
  }

  private void throwNet(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new VFXAction(new EntangleEffect(p.hb.cX - 70.0F * Settings.scale, p.hb.cY + 10.0F * Settings.scale, m.hb.cX, m.hb.cY), 0.5F));
    CardCrawlGame.sound.play("POWER_ENTANGLED", 0.05F);
    // Destroy the byrds.
    this.addToBot(new RemoveSpecificPowerAction(m, p, FlightPower.POWER_ID));

    this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false)));
    this.addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -5), -5));
    this.addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, 5), 5));
  }

}
