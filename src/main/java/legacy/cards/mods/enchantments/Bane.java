package legacy.cards.mods.enchantments;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.cards.LegacyCard;
import legacy.util.MonsterUtils;

/**
 * Enchantment that causes weapons to apply frost.
 */
public class Bane extends Enchantment {

  public static final String ID = "legacy:bane";
  public final MonsterUtils.MonsterType monsterType;

  public Bane(MonsterUtils.MonsterType monsterType) {
    super(ID, "Bane", "Deals double damage to " + monsterType + "s.", LegacyCard.LegacyCardType.WEAPON, AbstractCard.CardRarity.RARE, 2, 4);

    this.monsterType = monsterType;
  }

  public Bane() {
    this(MonsterUtils.getRandomType());
  }

  @Override
  public float modifyDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
    if (!MonsterUtils.isMonsterType(target, this.monsterType)) return damage;

    return damage * 2;
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new Bane(this.monsterType);
  }

  @Override
  public Color getColor() {
    return Color.valueOf("#abcdef");
  }

}
