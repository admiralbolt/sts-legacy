package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ThornsPower;
import legacy.cards.LegacyCard;
import legacy.powers.LoseThornsPower;

/**
 * Enchantment that gives armor artifact.
 */
public class Spiked extends Enchantment {

  public static String ID = "legacy:spiked";
  public static int THORN_AMOUNT = 3;

  public Spiked() {
    super(ID, "Spiked", "Gain " + THORN_AMOUNT + " Temporary Thorns.", LegacyCard.LegacyCardType.ARMOR, AbstractCard.CardRarity.COMMON, 20, -4);
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ThornsPower(AbstractDungeon.player, THORN_AMOUNT), THORN_AMOUNT));
    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LoseThornsPower(AbstractDungeon.player, THORN_AMOUNT), THORN_AMOUNT));
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Spiked();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#98500d");
  }
}
