package legacy.cards.equipment.armor;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import legacy.LegacyMod;
import legacy.cards.mods.traits.LightArmorTrait;

/**
 * 0 cost light armor that also gives temporary dexterity.
 */
public class PaddedArmor extends LegacyArmor {

  public static final String ID = LegacyMod.makeID("padded_armor");
  public static final int COST = 0;

  public PaddedArmor() {
    super(ID, COST, CardRarity.COMMON, new LightArmorTrait());

    this.baseBlock = this.block = 2;
    this.baseMagicNumber = this.magicNumber = 1;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LoseDexterityPower(p, this.magicNumber), this.magicNumber));
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeBlock(1);
    this.upgradeMagicNumber(1);
    this.initializeDescription();
  }
}
