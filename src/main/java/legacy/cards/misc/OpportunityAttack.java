package legacy.cards.misc;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.LegacyCard;
import legacy.powers.FatiguePower;

/**
 * Deal damage but lose energy next turn.
 */
public class OpportunityAttack extends LegacyCard {

  public static final String ID = "legacy:opportunity_attack";
  public static final int COST = 0;

  public OpportunityAttack() {
    super(ID, COST, LegacyCardType.MISC, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

    this.baseDamage = this.damage = 7;
    this.magicNumber = this.baseMagicNumber = 1;
    this.damageTypeForTurn = DamageInfo.DamageType.NORMAL;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(4);
    this.upgradeMagicNumber(1);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    this.addToBot(new ApplyPowerAction(p, p, new FatiguePower(p, p, this.magicNumber), this.magicNumber));
  }
}
