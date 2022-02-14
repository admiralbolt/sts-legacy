package legacy.cards.prestige_classes;

import basemod.cardmods.EtherealMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.powers.ArchMagePower;

/**
 * Infinite power.
 */
public class ArchMage extends PrestigeClass {

  public static final String ID = "legacy:arch_mage";
  public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 3;

  public ArchMage() {
    super(ID, cardStrings, COST, CardRarity.RARE, 0, 0, 6);

    CardModifierManager.addModifier(this, new EtherealMod());
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    CardModifierManager.removeModifiersById(this, EtherealMod.ID, true);
    this.upgradeName();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    addToBot(new ApplyPowerAction(p, p, new ArchMagePower(p)));
  }
}
