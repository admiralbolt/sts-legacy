package legacy.cards.equipment.weapons;

import legacy.cards.mods.traits.FlurryTrait;
import legacy.patches.LegacyCardTags;

/**
 * A 0 cost flurry weapon.
 */
public class Dagger extends LegacyWeapon {

  public static final String ID = "legacy:dagger";
  public static final int COST = 0;

  public Dagger() {
    super(ID, COST, CardRarity.COMMON, CardTarget.ENEMY, new FlurryTrait());

    this.tags.add(LegacyCardTags.FLURRY);
    this.baseDamage = this.damage = 2;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(2);
    this.initializeDescription();
  }

}

