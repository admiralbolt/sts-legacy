package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import legacy.cards.LegacyCard;
import legacy.powers.ReflectionPower;

/**
 * Enchantment that gives 1 plated armor.
 */
public class Mirrored extends Enchantment {

  public static String ID = "legacy:mirrored";

  public Mirrored() {
    super(ID, "Mirrored", "Gain 1 legacy:Reflection.", LegacyCard.LegacyCardType.ARMOR, AbstractCard.CardRarity.UNCOMMON, 5, 3);
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ReflectionPower(AbstractDungeon.player,1), 1));
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Mirrored();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#aaaaaa");
  }
}
