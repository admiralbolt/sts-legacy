package legacy.cards.equipment.weapons;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.LegacyMod;
import legacy.actions.PermanentBuffAction;

public class Anathema extends LegacyWeapon {

  public static final String ID = LegacyMod.makeID("anathema");
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 2;

  public Anathema() {
    super(ID, cardStrings, COST, CardRarity.RARE, CardTarget.ENEMY);

    this.enchantable = false;
  }

  // Actions the card should do.
  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.player.decreaseMaxHealth(1);
    AbstractDungeon.actionManager.addToBottom(new PermanentBuffAction(this.cardID, this.magicNumber));
    AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(10);
    this.initializeDescription();
  }

}

