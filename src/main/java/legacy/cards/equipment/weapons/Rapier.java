package legacy.cards.equipment.weapons;

import legacy.LegacyMod;
import legacy.cards.mods.traits.FinesseTrait;

public class Rapier extends LegacyWeapon {

  public static final String ID = LegacyMod.makeID("rapier");
  public static final int COST = 1;

  public Rapier() {
    super(ID, COST, CardRarity.COMMON, CardTarget.ENEMY, new FinesseTrait());

    this.baseDamage = this.damage = 4;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(3);
    this.initializeDescription();
  }

}

