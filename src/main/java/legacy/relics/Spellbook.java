package legacy.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

/**
 * Data disk! But a spellbook!
 */
public class Spellbook extends LegacyRelic {

  public static final String RELIC_ID = "legacy:spellbook";

  public Spellbook() {
    super(RELIC_ID, RelicTier.COMMON, LandingSound.MAGICAL, false);
  }

  @Override
  public AbstractRelic makeCopy() {
    return new Spellbook();
  }

  @Override
  public void atBattleStart() {
    this.flash();
    this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, 1), 1));
    this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
  }

}