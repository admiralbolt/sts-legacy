package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import legacy.cards.LegacyCard;

/**
 * Enchantment that causes weapons to gain 1 energy next turn.
 */
public class Energizing extends Enchantment {

  public static final String ID = "legacy:energizing";

  public Energizing() {
    super(ID, "Energizing", "Gain [E] next turn.", LegacyCard.LegacyCardType.WEAPON, AbstractCard.CardRarity.UNCOMMON, 6, 3);
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergizedPower(AbstractDungeon.player, 1), 1));
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Energizing();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#ee6a31");
  }

}
