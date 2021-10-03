package legacy.enchantments;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

/**
 * Enchantment that causes weapons to apply poison.
 */
public class Corrosive extends Enchantment {

  public Corrosive() {
    super("legacy:corrosive", "Corrosive", "Apply 3 Poison.", AbstractCard.CardType.ATTACK);
  }

  @Override
  public void apply(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new PoisonPower(m, p, 3), 3));
  }
}
