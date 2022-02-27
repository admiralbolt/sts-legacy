package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import legacy.cards.LegacyCard;

/**
 * Enchantment that causes weapons to draw a card!
 */
public class Speedy extends Enchantment {

  public static final String ID = "legacy:speedy";

  public Speedy() {
    super(ID, "Speedy", "Draw a card.", LegacyCard.LegacyCardType.WEAPON, AbstractCard.CardRarity.UNCOMMON, 6, 2);
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    this.addToBot(new DrawCardAction(1));
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Speedy();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#91ff33");
  }

}
