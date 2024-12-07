package legacy.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

/**
 * Stunning requires more stacks of lightning.
 */
public class LightningResistancePower extends LegacyPower {

  public static final String POWER_ID = "legacy:lightning_resistance_power";

  public LightningResistancePower(AbstractCreature owner, int amount) {
    super(POWER_ID, owner, amount, PowerType.BUFF);
  }

}
