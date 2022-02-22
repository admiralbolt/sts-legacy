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
import com.megacrit.cardcrawl.powers.PoisonPower;
import legacy.cards.LegacyCards;
import legacy.cards.mods.enchantments.Enchantment;
import legacy.powers.FrostPower;

/**
 * Enchantment that causes weapons to apply poison.
 */
public class Corrosive extends Enchantment {

  private static final int POISON_AMOUNT = 4;

  public Corrosive() {
    super("legacy:corrosive", "Corrosive", "Apply " + POISON_AMOUNT + " Poison.", LegacyCards.EquipmentType.WEAPON, 10, -2);
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new PoisonPower(target, AbstractDungeon.player, POISON_AMOUNT), POISON_AMOUNT));
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Corrosive();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#4db560");
  }

}
