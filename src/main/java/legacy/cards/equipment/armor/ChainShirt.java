package legacy.cards.equipment.armor;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.mods.traits.LightArmorTrait;

/**
 * 0 cost light armor.
 */
public class ChainShirt extends LegacyArmor {

  public static final String ID = "legacy:chain_shirt";
  public static final int COST = 0;

  public ChainShirt() {
    super(ID, COST, CardRarity.UNCOMMON, new LightArmorTrait());

    this.baseBlock = this.block = 4;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeBlock(3);
    this.initializeDescription();
  }
}
