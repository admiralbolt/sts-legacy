package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import legacy.cards.LegacyCard;

/**
 * Enchantment that makes armor reduce 1 stack of vulnerable, frail, and weak.
 */
public class Cleansing extends Enchantment {

  public static String ID = "legacy:cleansing";

  public Cleansing() {
    super(ID, "Cleansing", "Remove 1 stack of Frail, Vulnerable, and Weak.", LegacyCard.LegacyCardType.ARMOR, AbstractCard.CardRarity.UNCOMMON, 6, 2);
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    this.addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, FrailPower.POWER_ID, 1));
    this.addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, VulnerablePower.POWER_ID, 1));
    this.addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, WeakPower.POWER_ID, 1));
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Cleansing();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#87aaee");
  }
}
