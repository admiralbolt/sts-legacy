package legacy.cards.equipment.weapons;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import legacy.LegacyMod;
import legacy.cards.mods.traits.FinesseTrait;
import legacy.cards.mods.traits.RangedTrait;

public class Whip extends LegacyWeapon {

  public static final String ID = LegacyMod.makeID("whip");
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  private static final int COST = 1;

  public Whip() {
    super(ID, cardStrings, COST, CardRarity.UNCOMMON, CardTarget.ENEMY, new RangedTrait());
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(2);
    this.upgradeMagicNumber(1);
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    super.use(p, m);
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
  }

}

