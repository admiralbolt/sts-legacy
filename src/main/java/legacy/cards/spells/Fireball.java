package legacy.cards.spells;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.powers.BurnPower;
import legacy.util.ReallyMisc;

import java.util.Arrays;

/**
 * Deal damage and apply burn to all enemies.
 */
public class Fireball extends Spell {

  public static final String ID = "legacy:fireball";
  public static final int COST = 2;

  public Fireball() {
    super(ID, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY, SpellSchool.EVOCATION, 3);

    this.baseDamage = this.damage = 8;
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
    this.upgradeDamage(3);
    this.upgradeMagicNumber(1);
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    System.out.println("player: " + p.name + ", multiDamage: " + Arrays.toString(this.multiDamage) + ", damageForTurn: " + this.damageTypeForTurn);
    this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
    ReallyMisc.getAllEnemies().forEach(monster -> {
      this.addToBot(new ApplyPowerAction(monster, p, new BurnPower(monster, this.magicNumber), this.magicNumber));
    });
  }

}
