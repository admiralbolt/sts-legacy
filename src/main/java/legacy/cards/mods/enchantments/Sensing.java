package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ThornsPower;
import legacy.cards.LegacyCard;
import legacy.powers.LoseThornsPower;

/**
 * Enchantment that gives armor scry 2.
 */
public class Sensing extends Enchantment {

  public static String ID = "legacy:Sensing";
  public static int SCRY_AMOUNT = 2;

  public Sensing() {
    super(ID, "Sensing", "Scry " + SCRY_AMOUNT + ".", LegacyCard.LegacyCardType.ARMOR, AbstractCard.CardRarity.COMMON, 20, -4);
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    this.addToBot(new ScryAction(SCRY_AMOUNT));
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Sensing();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#179ea8");
  }
}
