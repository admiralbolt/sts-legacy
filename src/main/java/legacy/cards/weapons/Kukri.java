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
import legacy.cards.mods.traits.FinesseTrait;
import legacy.cards.mods.traits.FlurryTrait;

public class Kukri extends LegacyWeapon {

  public static final String ID = LegacyMod.makeID("kukri");
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 0;

  public Kukri() {
    super(ID, cardStrings, COST, CardRarity.COMMON, CardTarget.ENEMY, new FlurryTrait(), new FinesseTrait());
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    super.use(p, m);

    AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
  }

}

