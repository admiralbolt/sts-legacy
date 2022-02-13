package legacy.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Reading the while loop nested inside an infinite while loop inside of RemoveAllPowersAction() is hurting me so much
 * that I'm going to just write a readable version.
 */
public class BetterRemoveAllPowersAction extends AbstractGameAction {

  private AbstractCreature target;
  private Set<AbstractPower.PowerType> powerTypes;

  public BetterRemoveAllPowersAction(AbstractCreature target) {
    this(target, AbstractPower.PowerType.BUFF, AbstractPower.PowerType.DEBUFF);
  }
  public BetterRemoveAllPowersAction(AbstractCreature target, AbstractPower.PowerType... types) {
    this.target = target;
    this.powerTypes = new HashSet<>(Arrays.asList(types));
  }

  @Override
  public void update() {
    for (AbstractPower power : this.target.powers) {
      if (!this.powerTypes.contains(power.type)) continue;

      this.addToTop(new RemoveSpecificPowerAction(this.target, this.target, power.ID));
    }

    this.isDone = true;
  }
}
