package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * Enchantments are a special type of modifier that gets applied only to weapons & armor.
 */
public class Enchantment extends AbstractCardModifier {

  public String id;
  public String name;
  public String description;
  public AbstractCard.CardType type;

  public Enchantment(String id, String name, String description, AbstractCard.CardType type) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.type = type;
  }

  public String toDatabaseString() {
    return String.format("('%s', '%s', '%s')", this.id, this.name, this.description);
  }

  @Override
  public String modifyDescription(String rawDescription, AbstractCard card) {
    return rawDescription + " NL " + this.description;
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return null;
  }

  @Override
  public String identifier(AbstractCard card) {
    return this.id;
  }

  public Color getColor() {
    return null;
  }

}
