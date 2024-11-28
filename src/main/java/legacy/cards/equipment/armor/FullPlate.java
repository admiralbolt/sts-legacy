package legacy.cards.equipment.armor;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import legacy.cards.mods.traits.HeavyArmorTrait;

public class FullPlate extends LegacyArmor {

  public static final String ID = "legacy:full_plate";
  public static final int COST = 2;

  public FullPlate() {
    super(ID, COST, CardRarity.UNCOMMON, new HeavyArmorTrait());

    this.baseBlock = this.block = 7;
    this.baseMagicNumber = this.magicNumber = 4;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new GainBlockAction(p, p, this.block));
    this.addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, this.magicNumber), this.magicNumber));
    this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, -1), -1));
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
