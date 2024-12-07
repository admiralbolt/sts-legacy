package legacy.cards.misc;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import legacy.cards.LegacyCard;
import legacy.patches.LegacyCardTags;
import legacy.powers.BleedPower;
import legacy.powers.StealthPower;

public class Hamstring extends LegacyCard {

  public static final String ID = "legacy:hamstring";
  public static final int COST = 2;

  public Hamstring() {
    super(ID, COST, LegacyCardType.MISC, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY, new StatRequirements(0, 4, 0));

    this.tags.add(LegacyCardTags.REQUIRES_STEALTH);

    this.baseDamage = this.damage = 7;
    this.baseMagicNumber = this.magicNumber = 3;
    this.baseMagicNumberTwo = this.magicNumberTwo = 1;
    this.damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    this.exhaust = true;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(3);
    this.upgradeMagicNumber(2);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    this.addToBot(new ApplyPowerAction(m, p, new BleedPower(m, this.magicNumber), this.magicNumber));
    this.addToBot(new ApplyPowerAction(m, p, new SlowPower(m, this.magicNumberTwo), this.magicNumberTwo));
  }

  @Override
  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    if (!super.canUse(p, m)) return false;

    AbstractPower stealth = p.getPower(StealthPower.POWER_ID);
    return stealth != null && stealth.amount > 0;
  }

}
