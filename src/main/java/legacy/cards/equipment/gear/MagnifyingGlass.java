package legacy.cards.equipment.gear;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.actions.MagnifyingGlassAction;
import legacy.cards.LegacyCard;

/**
 * Reduce the cost of a card in hand by 1 for the rest of combat.
 * Upgrades to two cards.
 */
public class MagnifyingGlass extends LegacyCard {

  public static final String ID = "legacy:magnifying_glass";
  public static final int COST = 1;

  public MagnifyingGlass() {
    super(ID, COST, LegacyCardType.GEAR, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

    this.baseMagicNumber = this.magicNumber = 1;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeMagicNumber(1);
    this.upgradeName();
    this.rawDescription = this.cardStrings.UPGRADE_DESCRIPTION;
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new MagnifyingGlassAction(this.magicNumber));
  }

}
