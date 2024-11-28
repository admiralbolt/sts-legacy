package legacy.cards.equipment.armor;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.mods.traits.MediumArmorTrait;

/**
 * Medium armor that blocks twice!
 */
public class Chainmail extends LegacyArmor {

  public static final String ID = "legacy:chainmail";
  public static final int COST = 2;

  public Chainmail() {
    super(ID, COST, CardRarity.UNCOMMON, new MediumArmorTrait());

    this.baseBlock = this.block = 4;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeBlock(2);
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new GainBlockAction(p, this.block));
    this.addToBot(new GainBlockAction(p, this.block));
  }
}
