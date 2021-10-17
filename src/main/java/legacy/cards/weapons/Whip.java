package legacy.cards.weapons;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.LegacyMod;
import legacy.actions.PiercingDamageAction;

public class Whip extends LegacyWeapon {

  public static final String ID = LegacyMod.makeID("whip");
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 1;

  public Whip() {
    super(ID, cardStrings, COST, CardRarity.UNCOMMON, CardTarget.ENEMY, WeaponTrait.FINESSE, WeaponTrait.RANGED);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new PiercingDamageAction(m, p, damage));
    super.use(p, m);
  }

}

