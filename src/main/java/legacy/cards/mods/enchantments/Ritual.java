package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import legacy.cards.LegacyCard;
import legacy.powers.FrostPower;
import legacy.util.CardUtils;
import legacy.util.ReallyMisc;

/**
 * Enchantment that causes weapons to gain 3 damage for the rest of the run when fatal.
 */
public class Ritual extends Enchantment {

  public static final String ID = "legacy:ritual";
  private static final int RITUAL_AMOUNT = 3;

  public Ritual() {
    super(ID, "Icy", "If Fatal, increase this card's damage by 3 for the rest of the run. Exhaust", LegacyCard.LegacyCardType.WEAPON, AbstractCard.CardRarity.RARE, 3, 1);
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    if (CardUtils.isAOE(card)) {
      ReallyMisc.getAllEnemies().forEach(m -> {
        this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new FrostPower(m, RITUAL_AMOUNT), RITUAL_AMOUNT));
      });
      return;
    }
    this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new FrostPower(target, RITUAL_AMOUNT), RITUAL_AMOUNT));
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Ritual();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#91faf9");
  }

}
