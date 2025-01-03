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
 * Enchantment that causes weapons to apply frost.
 */
public class Icy extends Enchantment {

  public static final String ID = "legacy:icy";
  private static final int FROST_AMOUNT = 2;

  public Icy() {
    super(ID, "Icy", "Apply " + FROST_AMOUNT + " legacy:Frost.", LegacyCard.LegacyCardType.WEAPON, AbstractCard.CardRarity.COMMON, 16, -3);
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    if (CardUtils.isAOE(card)) {
      ReallyMisc.getAllEnemies().forEach(m -> {
        this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new FrostPower(m, FROST_AMOUNT), FROST_AMOUNT));
      });
      return;
    }
    this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new FrostPower(target, FROST_AMOUNT), FROST_AMOUNT));
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Icy();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#71aaf9");
  }

}
