package legacy.cards.weapons;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.CommonKeywordIconsField;
import com.evacipated.cardcrawl.mod.stslib.patches.CommonKeywordIconsPatches;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.LegacyMod;
import legacy.cards.mods.traits.TwoHandedTrait;

public class Greatsword extends LegacyWeapon {

  public static final String ID = LegacyMod.makeID("greatsword");
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 2;

  public Greatsword() {
    super(ID, cardStrings, COST, CardRarity.COMMON, CardTarget.ENEMY, WeaponTrait.TWO_HANDED);
    CardModifierManager.addModifier(this, new TwoHandedTrait());
    this.exhaust = true;
    this.isInnate = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    super.use(p, m);
  }

}

