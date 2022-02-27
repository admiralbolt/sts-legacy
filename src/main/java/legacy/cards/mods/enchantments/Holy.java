package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import legacy.cards.LegacyCard;

/**
 * Enchantment that causes weapons to gain healing!
 */
public class Holy extends Enchantment {

  public static final String ID = "legacy:holy";
  public static final int HEAL_AMOUNT = 2;

  public Holy() {
    super(ID, "Holy", "Heal " + HEAL_AMOUNT + " HP.", LegacyCard.LegacyCardType.WEAPON, AbstractCard.CardRarity.UNCOMMON, 6, 2);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Holy();
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    this.addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, HEAL_AMOUNT));
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#eeeeee");
  }

}
