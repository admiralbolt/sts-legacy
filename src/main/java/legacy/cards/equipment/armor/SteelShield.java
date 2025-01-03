package legacy.cards.equipment.armor;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.LegacyMod;
import legacy.cards.mods.traits.MediumArmorTrait;

public class SteelShield extends LegacyArmor {

  public static final String ID = LegacyMod.makeID("steel_shield");
  public static final int COST = 1;

  public SteelShield() {
    super(ID, COST, CardRarity.UNCOMMON, new MediumArmorTrait());

    this.baseBlock = this.block = 4;
    this.baseMagicNumber = this.magicNumber = 2;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(p, p, this.magicNumber));
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeBlock(2);
    this.upgradeMagicNumber(1);
    this.initializeDescription();
  }
}
