package legacy.enchantments;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.powers.FrostPower;

/**
 * Enchantment that causes weapons to apply frost.
 */
public class Icy extends Enchantment {

  public Icy() {
    super("legacy:icy", "Icy", "Apply 2 legacy:Frost.", AbstractCard.CardType.ATTACK);
  }

  @Override
  public void apply(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new FrostPower(m, 2), 2));
  }
}
