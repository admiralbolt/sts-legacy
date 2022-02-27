package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import legacy.cards.LegacyCard;
import legacy.powers.LightningPower;
import legacy.util.CardUtils;
import legacy.util.ReallyMisc;

/**
 * Enchantment that causes weapons to apply lightning.
 */
public class Shocking extends Enchantment {

  public static final String ID = "legacy:shocking";
  private static final int SHOCK_AMOUNT = 4;

  public Shocking() {
    super(ID, "Shocking", "Apply " + SHOCK_AMOUNT + " legacy:Lightning.", LegacyCard.LegacyCardType.WEAPON, AbstractCard.CardRarity.COMMON, 16, -4);
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    if (CardUtils.isAOE(card)) {
      ReallyMisc.getAllEnemies().forEach(m -> {
        this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new LightningPower(target, SHOCK_AMOUNT), SHOCK_AMOUNT));
      });
      return;
    }
    this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new LightningPower(target, SHOCK_AMOUNT), SHOCK_AMOUNT));
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Shocking();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#ffdd33");
  }

}
