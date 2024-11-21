package legacy.cards.spells;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * Exhaust a card from your discard to gain block.
 */
public class BoneArmor extends Spell {

  public static final String ID = "legacy:bone_armor";
  public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final int COST = 1;

  public BoneArmor() {
    super(ID, cardStrings, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF, SpellSchool.NECROMANCY, 1);

    this.baseBlock = this.block = 6;
  }


  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeBlock(3);
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    // Thanks STSLib! Also I should've read the docs first before rolling my own exhaust from discard action :(
    // One other thing to note here, is that it doesn't have as nice of a UI as the hand select.
    this.addToBot(new MoveCardsAction(p.exhaustPile, p.discardPile, 1));
    this.addToBot(new GainBlockAction(p, this.block));
  }

  // Additionally requires a card in the discard in order to cast.
  @Override
  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    if (!super.canUse(p, m)) return false;

    return !p.discardPile.group.isEmpty();
  }
}
