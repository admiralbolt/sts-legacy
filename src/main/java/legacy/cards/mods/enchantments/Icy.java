package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.LegacyCards;
import legacy.cards.mods.enchantments.Enchantment;
import legacy.powers.FrostPower;

/**
 * Enchantment that causes weapons to apply frost.
 */
public class Icy extends Enchantment {

  private static final int FROST_AMOUNT = 2;

  public Icy() {
    super("legacy:icy", "Icy", "Apply " + FROST_AMOUNT + " legacy:Frost.", LegacyCards.EquipmentType.WEAPON, 10, -2);
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new FrostPower(target, FROST_AMOUNT), FROST_AMOUNT));
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Icy();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#d6ecef");
  }

}
