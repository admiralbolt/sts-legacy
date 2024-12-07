package legacy.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

/**
 * +1 all stats!
 */
public class OmniCharm extends LegacyRelic {

  public static final String RELIC_ID = "legacy:omni_charm";

  public OmniCharm() {
    super(RELIC_ID, RelicTier.RARE, LandingSound.MAGICAL, false);
  }

  @Override
  public AbstractRelic makeCopy() {
    return new OmniCharm();
  }

  @Override
  public void atBattleStart() {
    this.flash();
    this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
    this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
    this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, 1), 1));
    this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
  }

}