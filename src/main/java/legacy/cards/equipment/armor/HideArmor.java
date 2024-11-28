package legacy.cards.equipment.armor;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import legacy.cards.mods.traits.MediumArmorTrait;

/**
 * Medium armor that blocks for a little next turn.
 */
public class HideArmor extends LegacyArmor {

  public static final String ID = "legacy:hide_armor";
  public static final int COST = 1;

  public HideArmor() {
    super(ID, COST, CardRarity.COMMON, new MediumArmorTrait());

    this.baseBlock = this.block = 5;
    this.baseMagicNumber = this.magicNumber = 2;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeBlock(3);
    this.upgradeMagicNumber(1);
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new GainBlockAction(p, this.block));
    this.addToBot(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, this.magicNumber), this.magicNumber));
  }

  @Override
  public void applyPowersToBlock() {
    super.applyPowersToBlock();

    // We also want to apply up to 3 Dexterity to the next turn block.
    AbstractPower dexterity = AbstractDungeon.player.getPower("Dexterity");
    if (dexterity == null || dexterity.amount == 0) return;

    this.magicNumber = this.baseMagicNumber + Math.min(dexterity.amount, 3);
    this.isMagicNumberModified = true;
  }
}
