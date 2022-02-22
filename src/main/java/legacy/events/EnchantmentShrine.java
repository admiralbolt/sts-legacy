package legacy.events;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import legacy.cards.LegacyCard;
import legacy.cards.mods.enchantments.Enchantment;
import legacy.cards.mods.enchantments.EnchantmentUtils;
import legacy.util.CardUtils;

import java.util.List;

/**
 * A shiny shrine event that enchants a random card in your deck.
 */
public class EnchantmentShrine extends AbstractImageEvent {

  public static final String ID = "legacy:enchantment_shrine";
  public static final EventStrings EVENT_STRINGS = CardCrawlGame.languagePack.getEventString(ID);

  private final List<LegacyCard> enchantableCards;

  public EnchantmentShrine() {
    super(EVENT_STRINGS.NAME, EVENT_STRINGS.DESCRIPTIONS[0], "legacy/images/events/enchantment_shrine.png");

    enchantableCards = EnchantmentUtils.getEnchantableCards();
    // Only allow the dialogue option to enchant IF the deck has enchantable cards.
    if (enchantableCards.size() > 0) {
      this.imageEventText.setDialogOption(EVENT_STRINGS.OPTIONS[0]);
    } else {
      this.imageEventText.setDialogOption(EVENT_STRINGS.OPTIONS[2], true);
    }
    this.imageEventText.setDialogOption(EVENT_STRINGS.OPTIONS[1]);
  }

  @Override
  public void onEnterRoom() {
    CardCrawlGame.music.playTempBGM("SHRINE");
  }

  @Override
  protected void buttonEffect(int buttonPressed) {
    // If we're finished here, just open the map and exit.
    if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMPLETE) {
      this.openMap();
      return;
    }

    switch(buttonPressed) {
      case 0:
        // Do an enchantment!
        // For now make it all random!
        (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMPLETE;
        LegacyCard card = this.enchantableCards.get(CardUtils.RANDOM.nextInt(this.enchantableCards.size()));
        List<Enchantment> enchantments = EnchantmentUtils.randomEnchantments(card, 1);
        CardModifierManager.addModifier(card, enchantments.get(0));
        card.updateName();
        AbstractDungeon.player.bottledCardUpgradeCheck(card);
        AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F - 190.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F));

        this.imageEventText.updateBodyText(EVENT_STRINGS.DESCRIPTIONS[1]);
        this.imageEventText.updateDialogOption(0, EVENT_STRINGS.OPTIONS[1]);
        this.imageEventText.clearRemainingOptions();
        break;
      case 1:
        // We skipped this shrine for some reason. :)
        (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMPLETE;
        this.imageEventText.updateBodyText(EVENT_STRINGS.DESCRIPTIONS[2]);
        this.imageEventText.updateDialogOption(0, EVENT_STRINGS.OPTIONS[1]);
        this.imageEventText.clearRemainingOptions();
        this.openMap();
    }
  }
}
