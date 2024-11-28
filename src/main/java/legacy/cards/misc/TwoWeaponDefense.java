package legacy.cards.misc;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import legacy.cards.LegacyCard;
import legacy.patches.LegacyCardTags;
import legacy.powers.FlurryPower;

/**s
 * A block card that scales with flurry & dex.
 */
public class TwoWeaponDefense extends LegacyCard {

  public static final String ID = "legacy:two_weapon_defense";
  public static final int COST = 1;

  public TwoWeaponDefense() {
    super(ID, COST, LegacyCardType.MISC, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

    this.tags.add(LegacyCardTags.FLURRY);
    this.baseBlock = this.block = 5;
    this.baseMagicNumber = this.magicNumber = 1;
  }

  @Override
  public void upgrade() {
    if (this.upgraded) return;

    this.upgradeName();
    this.upgradeMagicNumber(1);
    this.rawDescription = this.cardStrings.UPGRADE_DESCRIPTION;
    this.initializeDescription();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new GainBlockAction(p, p, this.block));
  }

  @Override
  protected void applyPowersToBlock() {
    super.applyPowersToBlock();

    AbstractPower flurry = AbstractDungeon.player.getPower(FlurryPower.POWER_ID);
    if (flurry == null) return;

    this.block += flurry.amount * this.magicNumber;
  }
}
