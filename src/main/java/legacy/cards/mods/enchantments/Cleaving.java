package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import legacy.cards.LegacyCard;
import legacy.util.ReflectionUtils;

/**
 * Enchantment that causes weapons to hit ALL enemies.
 */
public class Cleaving extends Enchantment {

  public static final String ID = "legacy:cleaving";

  public Cleaving() {
    super(ID, "Cleaving", "Hits ALL enemies.", LegacyCard.LegacyCardType.WEAPON, AbstractCard.CardRarity.UNCOMMON, 6, 4);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Cleaving();
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#ed33ff");
  }

  @Override
  public void onInitialApplication(AbstractCard card) {
    super.onInitialApplication(card);
    card.target = AbstractCard.CardTarget.ALL_ENEMY;
    ReflectionUtils.setFieldValue(card, "isMultiDamage", true);
  }
}
