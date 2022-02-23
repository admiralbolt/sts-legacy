package legacy.actions;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import legacy.cards.LegacyCard;
import legacy.cards.mods.enchantments.WeaponPlusX;
import legacy.util.CardUtils;

import java.util.ArrayList;

/**
 * Permanently increase the base damage of a card.
 *
 * This is done by doing two things:
 * 1. Applying a +X enchantment to modify the base damage of all cards with the same id. This will modify the
 *    damage of a card within the context of the currently loaded run.
 * 2. Queue a change to save the modification to the enchantments config file to persist it for future runs.
 */
public class PermanentBuffWeaponAction extends AbstractGameAction {

  private final LegacyCard card;
  private final int increase;

  public PermanentBuffWeaponAction(LegacyCard card, int increase) {
    this.card = card;
    this.increase = increase;
  }

  private void upgradeBonus(AbstractCard cardToUpgrade, int currentBonus) {
    if (CardModifierManager.hasModifier(cardToUpgrade, WeaponPlusX.ID)) {
      CardModifierManager.removeModifiersById(cardToUpgrade, WeaponPlusX.ID, true);
    }
    CardModifierManager.addModifier(cardToUpgrade, new WeaponPlusX(currentBonus + this.increase));
  }

  public void update() {
    int currentBonus = 0;
    // Check to see if we currently have a +X modifier on our card already.
    if (CardModifierManager.hasModifier(this.card, WeaponPlusX.ID)) {
      ArrayList<AbstractCardModifier> plusModifiers = CardModifierManager.getModifiers(this.card, WeaponPlusX.ID);
      currentBonus = ((WeaponPlusX) plusModifiers.get(0)).amount;
    }

    // Apply our enchantment to the cards in the master deck. This will persist for future combats.
    for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
      if (!card.cardID.equals(this.card.cardID)) continue;

      this.upgradeBonus(card, currentBonus);
    }

    // Apply our enchantment to ALL instances of the card in the current combat. This will handle permanently buffing
    // duplicates.
    for (AbstractCard card : CardUtils.getAllInstancesOfCardInCombat(this.card.cardID)) {
      this.upgradeBonus(card, currentBonus);
    }

    // We want to make sure we re-apply powers for our cads that have been modified in hand.
    for (AbstractCard card : AbstractDungeon.player.hand.group) {
      if (card.cardID.equals(this.card.cardID)) {
        card.applyPowers();
      }
    }

    this.isDone = true;
  }
}
