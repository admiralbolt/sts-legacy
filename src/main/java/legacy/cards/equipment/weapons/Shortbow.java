package legacy.cards.equipment.weapons;

import legacy.cards.mods.traits.FinesseTrait;
import legacy.cards.mods.traits.RangedTrait;

public class Shortbow extends LegacyWeapon {

  public static final String ID = "legacy:shortbow";
  private static final int COST = 1;

  public Shortbow() {
    super(ID, COST, CardRarity.COMMON, CardTarget.ENEMY, new FinesseTrait(), new RangedTrait());

    this.baseDamage = this.damage = 5;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(3);
    this.initializeDescription();
  }

}

