package legacy.cards.equipment.weapons;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.actions.ScytheDamageAction;
import legacy.cards.mods.traits.TwoHandedTrait;

public class Scythe extends LegacyWeapon {

  public static final String ID = "legacy:scythe";
  public static final int COST = 2;

  public Scythe() {
    super(ID, COST, CardRarity.RARE, CardTarget.ENEMY, new TwoHandedTrait());

    this.baseDamage = this.damage = 16;
    this.baseMagicNumber = this.magicNumber = 1;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(10);
    this.upgradeMagicNumber(1);
    this.rawDescription = this.cardStrings.UPGRADE_DESCRIPTION;
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new ScytheDamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), this.magicNumber));
  }

}

