package legacy.enchantments;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class Enchantment {

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

  /**
   * Override this method to make an enchantment actually do something.
   */
  public abstract void apply(AbstractPlayer p, AbstractMonster m);

}
