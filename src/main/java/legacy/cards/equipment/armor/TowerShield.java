package legacy.cards.equipment.armor;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import legacy.cards.mods.traits.HeavyArmorTrait;
import legacy.powers.CoverPower;

/**
 * BEEG Shield.
 *
 * Reduces dexterity, but gives big block and shield power.
 */
public class TowerShield extends LegacyArmor {

  public static final String ID = "legacy:tower_shield";
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 3;

  public TowerShield() {
    super(ID, cardStrings, COST, CardRarity.RARE, new HeavyArmorTrait());

    this.baseBlock = this.block = 15;
    this.baseMagicNumber = this.magicNumber = 2;
    this.baseMagicNumberTwo = this.magicNumberTwo = 1;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new GainBlockAction(p, p, this.block));
    this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, -2), -2));
    this.addToBot(new ApplyPowerAction(p, p, new BlurPower(p, this.magicNumber)));
    this.addToBot(new ApplyPowerAction(p, p, new CoverPower(p, this.magicNumberTwo)));
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeMagicNumber(1);
    this.upgradeMagicNumberTwo(1);
    this.initializeDescription();
  }
}
