package legacy.cards.equipment.weapons;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.LegacyMod;
import legacy.cards.mods.traits.FlurryTrait;
import legacy.patches.LegacyCardTags;

/**
 * A 0 cost flurry weapon.
 */
public class Nunchaku extends LegacyWeapon {

  public static final String ID = LegacyMod.makeID("nunchaku");
  public static final int COST = 1;

  public Nunchaku() {
    super(ID, COST, CardRarity.UNCOMMON, CardTarget.ENEMY, new FlurryTrait());

    this.tags.add(LegacyCardTags.FLURRY);
    this.baseDamage = this.damage = 3;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeDamage(2);
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    super.use(p, m);
    AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
  }

}

