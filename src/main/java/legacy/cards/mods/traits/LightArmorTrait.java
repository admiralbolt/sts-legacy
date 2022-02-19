package legacy.cards.mods.traits;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import legacy.cards.mods.ModifierWithBadge;
import legacy.util.TextureLoader;

/**
 * Light armor scales normally with dex. Playing light armor cards grant 1 Temporary Dexterity.
 */
public class LightArmorTrait extends ModifierWithBadge {

  public static final String ID = "legacy:light_armor";
  public static final Texture BADGE = TextureLoader.getTexture("legacy/images/cards/mods/traits/light_armor.png");

  public LightArmorTrait() {
    super(ID);
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new LightArmorTrait();
  }
}
