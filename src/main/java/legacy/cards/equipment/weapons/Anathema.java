package legacy.cards.equipment.weapons;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.actions.PermanentBuffWeaponAction;
import legacy.cards.mods.traits.TwoHandedTrait;

public class Anathema extends LegacyWeapon {

  public static final String ID = "legacy:anathema";
  public static final int COST = 2;

  public Anathema() {
    super(ID, COST, CardRarity.RARE, CardTarget.ENEMY, new TwoHandedTrait());

    this.baseDamage = this.damage = 15;
    this.enchantable = false;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.player.decreaseMaxHealth(1);
    AbstractDungeon.actionManager.addToBottom(new PermanentBuffWeaponAction(this, this.magicNumber));
    AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeMagicNumber(1);
    this.initializeDescription();
  }

}

