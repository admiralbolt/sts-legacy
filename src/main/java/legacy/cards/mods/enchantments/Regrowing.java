package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import legacy.cards.LegacyCard;

/**
 * Enchantment that gives 1 plated armor.
 */
public class Regrowing extends Enchantment {

  public static String ID = "legacy:regrowing";

  public Regrowing() {
    super(ID, "Regrowing", "Gain 1 Malleable.", LegacyCard.LegacyCardType.ARMOR, AbstractCard.CardRarity.RARE, 5, 4);
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MalleablePower(AbstractDungeon.player,1), 1));
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Regrowing();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#30dd81");
  }
}
