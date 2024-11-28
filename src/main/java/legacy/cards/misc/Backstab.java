package legacy.cards.misc;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import legacy.cards.LegacyCard;
import legacy.patches.LegacyCardTags;
import legacy.powers.StealthPower;

/**
 * Deal damage and draw a card. Requires stealth.
 */
public class Backstab extends LegacyCard {

  public static final String ID = "legacy:backstab";
  public static final int COST = 0;

  public Backstab() {
    super(ID, COST, LegacyCardType.MISC, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

    this.tags.add(LegacyCardTags.REQUIRES_STEALTH);
    this.baseDamage = this.damage = 7;
    this.magicNumber = this.baseMagicNumber = 1;
    this.damageTypeForTurn = DamageInfo.DamageType.NORMAL;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(4);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    this.addToBot(new DrawCardAction(this.magicNumber));
  }

  @Override
  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    if (!super.canUse(p, m)) return false;

    AbstractPower stealth = p.getPower(StealthPower.POWER_ID);
    return stealth != null && stealth.amount > 0;
  }

}
