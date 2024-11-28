package legacy.cards.equipment.weapons;

import legacy.LegacyMod;

public class Longsword extends LegacyWeapon {

  public static final String ID = LegacyMod.makeID("longsword");
  public static final int COST = 1;

  public Longsword() {
    super(ID, COST, CardRarity.COMMON, CardTarget.ENEMY);

    this.baseDamage = this.damage = 6;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(3);
    this.initializeDescription();
  }

}

