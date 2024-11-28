package legacy.cards.spells;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.actions.GraveStormAction;

/**
 * Fiend fire but for your graveyard.
 */
public class GraveStorm extends Spell {

  public static final String ID = "legacy:grave_storm";
  public static final int COST = 3;

  public GraveStorm() {
    super(ID, COST, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY, SpellSchool.NECROMANCY, 5);

    this.baseDamage = 5;
    this.exhaust = true;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeBaseCost(2);
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new GraveStormAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
  }

  // Additionally requires a card in the discard in order to cast.
  @Override
  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    if (!super.canUse(p, m)) return false;

    return !p.discardPile.group.isEmpty();
  }
}
