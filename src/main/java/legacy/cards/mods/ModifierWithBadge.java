package legacy.cards.mods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

/**
 * A card modifier that has a badge for rendering. We can't actually put the Texture class as a member though, because
 * of weird json serialization issues. The RenderCardModPatch checks for instances of this class to render.
 */
public abstract class ModifierWithBadge extends AbstractCardModifier {

  public String id;

  public ModifierWithBadge(String id) {
    this.id = id;
  }

  @Override
  public String identifier(AbstractCard card) {
    return this.id;
  }

  @Override
  public boolean shouldApply(AbstractCard card) {
    return !CardModifierManager.hasModifier(card, this.id);
  }
}
