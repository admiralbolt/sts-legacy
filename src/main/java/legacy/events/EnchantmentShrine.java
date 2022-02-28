package legacy.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import legacy.cards.LegacyCard;
import legacy.cards.mods.enchantments.Enchantment;
import legacy.cards.mods.enchantments.EnchantmentChoice;
import legacy.cards.mods.enchantments.EnchantmentUtils;
import legacy.util.CardUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A shiny shrine event that enchants a card in your deck.
 */
public class EnchantmentShrine extends AbstractImageEvent {

  public static final String ID = "legacy:enchantment_shrine";
  public static final EventStrings EVENT_STRINGS = CardCrawlGame.languagePack.getEventString(ID);

  private final List<LegacyCard> enchantableCards;
  private LegacyCard targetCard;

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
  public void update() {
    super.update();
    if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMPLETE) return;

    // We selected a card from the grid for enchanting. Generate enchantments for it and open the enchantment screen.
    if (targetCard == null && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
      this.targetCard = (LegacyCard) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
      // After the card is selected, roll some enchantments.
      List<Enchantment> enchantments = EnchantmentUtils.randomEnchantments(this.targetCard, 3);
      ArrayList<AbstractCard> enchantmentChoices = new ArrayList<>();
      enchantments.forEach(e -> enchantmentChoices.add(new EnchantmentChoice(e, this.targetCard)));
      AbstractDungeon.cardRewardScreen.chooseOneOpen(enchantmentChoices);
      AbstractDungeon.gridSelectScreen.selectedCards.clear();
      // We selected an enchantment! Clean up!
    } else if (this.targetCard != null && !AbstractDungeon.isScreenUp) {
      AbstractDungeon.player.bottledCardUpgradeCheck(this.targetCard);
      AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(this.targetCard.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F - 190.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F));
      (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMPLETE;
    }
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
        // Do an enchantment! Interesting logic is handled in update()
        AbstractDungeon.gridSelectScreen.open(CardUtils.makeCardGroup(this.enchantableCards), 1, EVENT_STRINGS.OPTIONS[3], false, false, false, false);
        this.imageEventText.updateBodyText(EVENT_STRINGS.DESCRIPTIONS[1]);
        this.imageEventText.updateDialogOption(0, EVENT_STRINGS.OPTIONS[1]);
        this.imageEventText.clearRemainingOptions();
        break;
      case 1:
        // We skipped this shrine for some reason. :)
        this.imageEventText.updateBodyText(EVENT_STRINGS.DESCRIPTIONS[2]);
        this.imageEventText.updateDialogOption(0, EVENT_STRINGS.OPTIONS[1]);
        this.imageEventText.clearRemainingOptions();
        this.openMap();
    }
  }
}
