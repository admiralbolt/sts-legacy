package legacy.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import legacy.cards.LegacyCard;

/**
 * Call this.updateName() on all legacy cards after initialization is done. This is to make sure card names are
 * properly updated *after* enchantments are loaded.
 */
@SpirePatch(clz=CardCrawlGame.class, method="loadPlayerSave")
public class UpdateCardNamesOnLoadPatch {

  public static void Postfix(CardCrawlGame __instance, AbstractPlayer p) {
    for (AbstractCard card : p.masterDeck.group) {
      if (!(card instanceof LegacyCard)) continue;

      ((LegacyCard) card).updateName();
    }
  }
}
