package legacy.cards.equipment.weapons;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import legacy.LegacyMod;
import legacy.cards.mods.traits.RangedTrait;

public class Whip extends LegacyWeapon {

  public static final String ID = LegacyMod.makeID("whip");
  private static final int COST = 1;

  public Whip() {
    super(ID, COST, CardRarity.UNCOMMON, CardTarget.ENEMY, new RangedTrait());

    this.baseDamage = this.damage = 3;
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

