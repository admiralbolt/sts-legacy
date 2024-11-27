package legacy.cards.mods.enchantments;

import basemod.abstracts.CustomCard;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.characters.TheAdventurer;

/**
 * Generate a card for choosing which enchantment to apply.
 */
public class EnchantmentChoice extends CustomCard {

  private final Enchantment enchantment;
  private final AbstractCard targetCard;

  public EnchantmentChoice(Enchantment enchantment, AbstractCard targetCard) {
    super(enchantment.id + "_choice", enchantment.name, "legacy/images/cards/enchantments/default_enchantment_choice.png",
            -2, enchantment.description, CardType.POWER, TheAdventurer.Enums.COLOR_GRAY, enchantment.rarity, CardTarget.NONE);

    this.enchantment = enchantment;
    this.targetCard = targetCard;
  }

  @Override
  public void onChoseThisOption() {
    EnchantmentUtils.enchantCard(this.targetCard, enchantment);
  }

  @Override
  public AbstractCard makeCopy() {
    return new EnchantmentChoice(this.enchantment, this.targetCard);
  }

  @Override
  public void upgrade() {
    // These cards shouldn't get upgraded.
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    // These cards shouldn't get used.
  }
}
