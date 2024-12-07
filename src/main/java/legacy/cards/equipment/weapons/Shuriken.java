package legacy.cards.equipment.weapons;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.mods.traits.FlurryTrait;
import legacy.cards.mods.traits.RangedTrait;
import legacy.patches.LegacyCardTags;

public class Shuriken extends LegacyWeapon {

  public static final String ID = "legacy:shuriken";
  public static final int COST = 0;
  public int copyAmount;

  public Shuriken() {
    super(ID, COST, CardRarity.UNCOMMON, CardTarget.ENEMY, new FlurryTrait(), new RangedTrait());

    this.tags.add(LegacyCardTags.FLURRY);
    this.copyAmount = 0;
    this.baseDamage = this.damage = 2;
    this.baseMagicNumber = this.magicNumber = 2;
  }

  public Shuriken(int copyAmount, int magicNumber, boolean isUpgraded) {
    super(ID, COST + copyAmount, CardRarity.UNCOMMON, CardTarget.ENEMY, new FlurryTrait(), new RangedTrait());

    this.tags.add(LegacyCardTags.FLURRY);
    this.baseDamage = this.damage = 2 + copyAmount * magicNumber;
    this.baseMagicNumber = this.magicNumber = magicNumber;
    this.exhaust = true;
    this.isEthereal = true;
    this.copyAmount = copyAmount;

    if (isUpgraded) {
      this.upgraded = true;
      this.upgradeName();
      this.initializeDescription();
    }
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeDamage(1);
    this.upgradeMagicNumber(1);
    this.upgradeName();
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    super.use(p, m);

    // Add a copy to hand that costs one more, has increased damage, exhaust and ethereal.
    Shuriken s = new Shuriken(this.copyAmount + 1, this.magicNumber, this.upgraded);
    this.addToBot(new MakeTempCardInHandAction(s));
  }

  @Override
  public AbstractCard makeStatEquivalentCopy() {
    AbstractCard card = super.makeStatEquivalentCopy();

    Shuriken s = (Shuriken) card;
    s.copyAmount = this.copyAmount;
    s.exhaust = this.exhaust;
    s.isEthereal = this.isEthereal;

    return s;
  }

}

