package legacy.cards.weapons;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import legacy.LegacyMod;

public class Rapier extends LegacyWeapon {

  public static final String ID = LegacyMod.makeID("rapier");
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 1;

  public Rapier() {
    super(ID, cardStrings, COST, CardRarity.COMMON, CardTarget.ENEMY, WeaponTrait.FINESSE);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    super.use(p, m);
  }

  @Override
  public void applyPowers() {
    // First, we remove any extra damage from Strength.
    AbstractPower strength = AbstractDungeon.player.getPower("Strength");
    int strengthAmount = 0;
    if (strength != null) {
      strengthAmount = strength.amount;
      strength.amount = 0;
    }
    super.applyPowers();

    if (strength != null && strengthAmount != 0) {
      strength.amount = strengthAmount;
    }

    // Then, we apply damage from Dexterity.
    AbstractPower dexterity = AbstractDungeon.player.getPower("Dexterity");
    if (dexterity != null && dexterity.amount != 0) {
      if (this.isMultiDamage && this.multiDamage != null) {
        for (int i = 0; i < this.multiDamage.length; ++i) {
          this.multiDamage[i] += dexterity.amount;
        }
      }
      System.out.println("Dexterity Amount: " + dexterity.amount);
      this.damage += dexterity.amount;
      this.isDamageModified = true;
    }
  }

  @Override
  public void calculateCardDamage(AbstractMonster mo) {
    // First, we remove any extra damage from Strength.
    AbstractPower strength = AbstractDungeon.player.getPower("Strength");
    int strengthAmount = 0;
    if (strength != null) {
      strengthAmount = strength.amount;
      strength.amount = 0;
    }
    super.calculateCardDamage(mo);

    if (strength != null && strengthAmount != 0) {
      strength.amount = strengthAmount;
    }

    // Then, we apply damage from Dexterity.
    AbstractPower dexterity = AbstractDungeon.player.getPower("Dexterity");
    if (dexterity != null && dexterity.amount != 0) {
      if (this.isMultiDamage && this.multiDamage != null) {
        for (int i = 0; i < this.multiDamage.length; ++i) {
          this.multiDamage[i] += dexterity.amount;
        }
      }
      this.damage += dexterity.amount;
    }
  }
}

