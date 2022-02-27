package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import legacy.cards.LegacyCard;

/**
 * Enchantment that gives armor artifact.
 */
public class HexGuard extends Enchantment {

  public static String ID = "legacy:hex_guard";
  public int amount;

  public HexGuard() {
    super(ID, "Hex Guard", "", LegacyCard.LegacyCardType.ARMOR, AbstractCard.CardRarity.RARE, 4, 4);
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ArtifactPower(AbstractDungeon.player,1), 1));
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new HexGuard();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#050649");
  }
}
