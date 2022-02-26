package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import legacy.cards.LegacyCard;
import legacy.powers.BurnPower;

/**
 * Enchantment that causes weapons to apply burn.
 */
public class Flaming extends Enchantment {

  public static final String ID = "legacy:flaming";
  private static final int BURN_AMOUNT = 3;

  public Flaming() {
    super(ID, "Flaming", "Apply " + BURN_AMOUNT + " legacy:Burn.", LegacyCard.LegacyCardType.WEAPON, AbstractCard.CardRarity.COMMON, 12, -2);
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new BurnPower(target, BURN_AMOUNT), BURN_AMOUNT));
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Flaming();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#cc5500");
  }

}
