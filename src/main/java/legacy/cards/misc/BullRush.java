package legacy.cards.misc;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import legacy.cards.LegacyCard;

/**
 * Deal damage and apply vulnerable.
 */
public class BullRush extends LegacyCard {

  public static final String ID = "legacy:bull_rush";
  public static final int COST = 1;

  public BullRush() {
    super(ID, COST, LegacyCardType.MISC, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

    this.baseDamage = this.damage = 3;
    this.magicNumber = this.baseMagicNumber = 1;
    this.damageTypeForTurn = DamageInfo.DamageType.NORMAL;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(1);
    this.upgradeMagicNumber(1);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
  }
}
