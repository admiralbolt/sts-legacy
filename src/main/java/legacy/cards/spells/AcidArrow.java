package legacy.cards.spells;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import legacy.powers.DelayedPoison;

/**
 * Apply 3 poison this turn and next.
 */
public class AcidArrow extends Spell {

  public static final String ID = "legacy:acid_arrow";
  public static final int COST = 1;

  public AcidArrow() {
    super(ID, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY, SpellSchool.CONJURATION, 2);

    this.baseMagicNumber = this.magicNumber = 3;
  }

  @Override
  public void applyPowers() {
    super.applyPowers(true);
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
    this.addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber, AbstractGameAction.AttackEffect.POISON));
    this.addToBot(new ApplyPowerAction(m, p, new DelayedPoison(m, p, this.magicNumber), this.magicNumber, AbstractGameAction.AttackEffect.POISON));
  }

}
