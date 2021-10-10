package legacy.cards.armor;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import legacy.LegacyMod;

public class SteelShield extends LegacyArmor {

  public static final String ID = LegacyMod.makeID("steel_shield");
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 1;

  public SteelShield() {
    super(ID, cardStrings, COST, CardRarity.UNCOMMON, CardTarget.SELF);

    this.baseMagicNumber = this.magicNumber = 2;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(p, p, this.magicNumber));
    super.use(p, m);
  }
}
