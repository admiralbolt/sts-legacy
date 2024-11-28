package legacy.cards.equipment.weapons;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import legacy.cards.mods.traits.FlurryTrait;
import legacy.patches.LegacyCardTags;
import legacy.powers.FlurryPower;

/**
 * A flurry weapon that gains bonus effects the more flurry stacks you have.
 * It's a "finisher" so it removes all flurry stacks on use.s
 */
public class Cestus extends LegacyWeapon {

  public static final String ID = "legacy:cestus";
  public static final int COST = 1;

  public Cestus() {
    super(ID, COST, CardRarity.RARE, CardTarget.ENEMY, new FlurryTrait());

    this.tags.add(LegacyCardTags.FLURRY);
    this.baseDamage = this.damage = 5;
    this.baseMagicNumber = this.magicNumber = 2;
    this.baseMagicNumberTwo = this.magicNumberTwo = 1;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(1);
    this.upgradeMagicNumber(1);
    this.upgradeMagicNumberTwo(1);
    this.initializeDescription();
  }


  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    super.use(p, m);

    AbstractPower flurry = AbstractDungeon.player.getPower(FlurryPower.POWER_ID);
    if (flurry == null) return;

    if (flurry.amount >= 2) {
      this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
      this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
      if (flurry.amount >= 4) {
        this.addToBot(new DrawCardAction(this.magicNumberTwo));
        if (flurry.amount >= 6) {
          this.addToBot(new StunMonsterAction(m, p));
        }
      }
    }

    this.addToBot(new RemoveSpecificPowerAction(p, p, FlurryPower.POWER_ID));
  }

}

