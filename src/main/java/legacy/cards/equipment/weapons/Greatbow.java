package legacy.cards.equipment.weapons;

import legacy.cards.mods.traits.FinesseTrait;
import legacy.cards.mods.traits.RangedTrait;
import legacy.cards.mods.traits.TwoHandedTrait;

public class Greatbow extends LegacyWeapon {

  public static final String ID = "legacy:greatbow";
  public static final int COST = 2;

  public Greatbow() {
    super(ID, COST, CardRarity.UNCOMMON, CardTarget.ENEMY, new TwoHandedTrait(), new FinesseTrait(), new RangedTrait());

    this.baseDamage = this.damage = 8;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(4);
    this.initializeDescription();
  }

}

