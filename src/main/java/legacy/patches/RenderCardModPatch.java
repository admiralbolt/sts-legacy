package legacy.patches;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;
import legacy.cards.mods.traits.EquipmentTrait;
import legacy.cards.mods.traits.TwoHandedTrait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.CommonKeywordIconsField;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Custom Patch to render card modifiers.
 *
 * This is mostly a duplicate of the STSLib CommonKeywordIconsPatches.java. We want to have special handling for
 * equipment traits vs. enchantments though, and want the rendering to be consistent across all views.
 * By default card mods seem to only get rendered during combat, and not in the library / single card view.
 * Additionally, we need the mods to be rendered nicely next to each other. Badges should be rendered so they
 * aren't on top of each other, which requires some math, and a single place where all mods are rendered.
 */
public class RenderCardModPatch {

  private static final Map<String, Texture> KEYWORD_MAP;
  static {
    KEYWORD_MAP = new HashMap<>();
    // BASE GAME KEYWORDS.
    KEYWORD_MAP.put(GameDictionary.INNATE.NAMES[0].toLowerCase(), StSLib.BADGE_INNATE);
    KEYWORD_MAP.put(GameDictionary.EXHAUST.NAMES[0].toLowerCase(), StSLib.BADGE_EXHAUST);
    KEYWORD_MAP.put(GameDictionary.ETHEREAL.NAMES[0].toLowerCase(), StSLib.BADGE_ETHEREAL);
    KEYWORD_MAP.put(GameDictionary.RETAIN.NAMES[0].toLowerCase(), StSLib.BADGE_RETAIN);
    KEYWORD_MAP.put("purge", StSLib.BADGE_PURGE);

    // EQUIPMENT TRAITS.
    KEYWORD_MAP.put("legacy:two_handed", TwoHandedTrait.BADGE);
  }

  @SpirePatch(clz=AbstractCard.class, method="renderCard")
  public static class RenderIcons {

    // Render badges in game.
    @SpireInsertPatch(locator=Locator.class)
    public static void patch(AbstractCard __instance, SpriteBatch sb, boolean hovered, boolean selected) {
      RenderBadges(sb, __instance);
    }

    private static class Locator extends SpireInsertLocator {
      @Override
      public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
        Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "renderType");
        return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
      }
    }
  }

  // Render badges in library.
  @SpirePatch(clz=AbstractCard.class, method="renderInLibrary")
  public static class RenderIconsInLibrary {

    @SpireInsertPatch(locator= Locator.class)
    public static void patch(AbstractCard instance, SpriteBatch sb) {
      RenderBadges(sb, instance);
    }

    private static class Locator extends SpireInsertLocator {
      @Override
      public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
        Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "renderType");
        return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
      }
    }
  }


  // Add Keyword powertips when the card uses the icons and make sure there are no duplicates
  @SpirePatch(clz=TipHelper.class, method="renderTipForCard")
  public static class RenderKeywords {
    @SpireInsertPatch(locator = Locator.class)
    public static void patch(AbstractCard c, SpriteBatch sb, @ByRef ArrayList<String>[] keywords) {
      keywords[0] = addKeywords(c, keywords[0]);
    }

    private static class Locator extends SpireInsertLocator {
      @Override
      public int[] Locate(CtBehavior ctBehavior) throws Exception {
        Matcher finalMatcher = new Matcher.FieldAccessMatcher(TipHelper.class, "KEYWORDS");
        return LineFinder.findInOrder(ctBehavior, finalMatcher);
      }
    }
  }

  // Render badges in popup.
  @SpirePatch(clz=SingleCardViewPopup.class, method="renderTips")
  public static class RenderKeywordsForSingleCardViewPopup {

    @SpireInsertPatch(locator=Locator.class)
    public static void patch(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
        ___card.keywords = addKeywords(___card, ___card.keywords);
    }

    private static class Locator extends SpireInsertLocator {
      @Override
      public int[] Locate(CtBehavior ctBehavior) throws Exception {
        Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "keywords");
        return LineFinder.findInOrder(ctBehavior, finalMatcher);
      }
    }
  }


  // Render icon on keyword powertips for clarity
  @SpirePatch(clz=TipHelper.class, method="renderBox")
  public static class RenderIconOnTips {
    @SpirePostfixPatch
    public static void patch(SpriteBatch sb, String word, float x, float y, AbstractCard ___card) {
      if (___card == null) return;

      drawBadgeOnTip(x, y, sb, KEYWORD_MAP.get(word));
    }
  }

  // Render in single card view madness.
  @SpirePatch(clz=SingleCardViewPopup.class, method="render")
  public static class SingleCardViewRenderIconOnCard {

    @SpireInsertPatch(locator=Locator.class)
    public static void patch(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card, Hitbox ___cardHb) {
      int offsetY = 0;

      // BASE GAME MODIFIERS.
      if (___card.isInnate) offsetY += drawBadge(sb, ___card, ___cardHb, StSLib.BADGE_INNATE, offsetY);
      if (___card.isEthereal) offsetY += drawBadge(sb, ___card, ___cardHb, StSLib.BADGE_ETHEREAL, offsetY);
      if (___card.retain || ___card.selfRetain) offsetY += drawBadge(sb, ___card, ___cardHb, StSLib.BADGE_RETAIN, offsetY);
      if (___card.purgeOnUse) offsetY += drawBadge(sb, ___card, ___cardHb, StSLib.BADGE_PURGE, offsetY);
      if (___card.exhaust || ___card.exhaustOnUseOnce) offsetY += drawBadge(sb, ___card, ___cardHb, StSLib.BADGE_EXHAUST, offsetY);

      // EQUIPMENT TRAITS.
      if (CardModifierManager.hasModifier(___card, "legacy:two_handed")) offsetY += drawBadge(sb, ___card, ___cardHb, TwoHandedTrait.BADGE, offsetY);
    }

    private static int drawBadge(SpriteBatch sb, AbstractCard card, Hitbox hb, Texture img, int offset) {
      float badge_w = img.getWidth();
      float badge_h = img.getHeight();
      sb.draw(img, hb.x + hb.width - (badge_w * Settings.scale) * 0.66f, hb.y + hb.height - (badge_h * Settings.scale) * 0.5f - ((offset * (badge_h * Settings.scale)*0.6f)), 0, 0, badge_w , badge_h ,
              Settings.scale, Settings.scale, card.angle, 0, 0, (int)badge_w, (int)badge_h, false, false);
      return 1;
    }

    private static class Locator extends SpireInsertLocator {
      @Override
      public int[] Locate(CtBehavior ctBehavior) throws Exception {
        Matcher finalMatcher = new Matcher.MethodCallMatcher(SingleCardViewPopup.class, "renderTips");
        return LineFinder.findInOrder(ctBehavior, finalMatcher);
      }
    }
  }

  private static boolean workaroundSwitch = false;

  @SpirePatch(clz=SingleCardViewPopup.class, method="renderTips")
  public static class DontAlwaysShowIconsPls {
    @SpireInsertPatch(locator=Locator.class, localvars={"t"})
    public static void patch(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card, ArrayList<PowerTip> t) {
      workaroundSwitch = true;
    }

    private static class Locator extends SpireInsertLocator {
      @Override
      public int[] Locate(CtBehavior ctBehavior) throws Exception {
        System.out.println("SINGLE CARD VIEW POPUP LOCATE");
        Matcher finalMatcher = new Matcher.MethodCallMatcher(TipHelper.class, "queuePowerTips");
        return LineFinder.findInOrder(ctBehavior, finalMatcher);
      }
    }
  }

  @SpirePatch(clz=TipHelper.class, method="renderPowerTips")
  public static class SingleCardViewRenderIconOnTips {
    @SpireInsertPatch(locator=Locator.class, localvars={"tip"})
    public static void patch(float x, float y, SpriteBatch sb, ArrayList<PowerTip> powerTips, PowerTip tip) {
      if (!workaroundSwitch) return;
      drawBadgeOnTip(x, y, sb, KEYWORD_MAP.get(tip.header.toLowerCase()));

      if (powerTips.get(powerTips.size() - 1).equals(tip)) workaroundSwitch = false;
    }

    private static class Locator extends SpireInsertLocator {
      @Override
      public int[] Locate(CtBehavior ctBehavior) throws Exception {
        Matcher finalMatcher = new Matcher.MethodCallMatcher(GlyphLayout.class, "setText");
        return LineFinder.findInOrder(ctBehavior, finalMatcher);
      }
    }
  }

  /**
   * Draws badges for ALL card modifiers. This has 3 categories:
   * 1. Base game modifiers (ethereal, innate, exhaust...).
   * 2. Weapon traits (two handed, ranged, finesse...).
   * 3. Enchantments (corrosive, icy, electric...).
   */
  private static void RenderBadges(SpriteBatch sb, AbstractCard card) {
    final float alpha = card.transparency;
    int offsetY = 0;

    // BASE GAME MODIFIERS.
    if (card.isInnate) offsetY -= RenderBadge(sb, card, StSLib.BADGE_INNATE, offsetY, alpha);
    if (card.isEthereal) offsetY -= RenderBadge(sb, card, StSLib.BADGE_ETHEREAL, offsetY, alpha);
    if (card.retain || card.selfRetain) offsetY -= RenderBadge(sb, card, StSLib.BADGE_RETAIN, offsetY, alpha);
    if (card.purgeOnUse) offsetY -= RenderBadge(sb, card, StSLib.BADGE_PURGE, offsetY, alpha);
    if (card.exhaust || card.exhaustOnUseOnce) offsetY -= RenderBadge(sb, card, StSLib.BADGE_EXHAUST, offsetY, alpha);

    // EQUIPMENT TRAITS.
    if (CardModifierManager.hasModifier(card, "legacy:two_handed")) offsetY -= RenderBadge(sb, card, TwoHandedTrait.BADGE, offsetY, alpha);
  }

  private static float RenderBadge(SpriteBatch sb, AbstractCard card, Texture texture, float offset_y, float alpha) {
    Vector2 offset = new Vector2(AbstractCard.RAW_W * 0.45f, AbstractCard.RAW_H * 0.45f + offset_y);
    DrawOnCardAuto(sb, card, texture, offset, 64, 64, Color.WHITE, alpha, 1);
    return 38;
  }

  // Add keywords to cards even if they don't have the text on them. This will save some space since we won't need to
  // include the names of every trait / enchantment on the card text.
  public static ArrayList<String> addKeywords(AbstractCard c, ArrayList<String> kws) {
    if (c.isInnate) kws.add(GameDictionary.INNATE.NAMES[0]);
    if (c.isEthereal) kws.add(GameDictionary.ETHEREAL.NAMES[0]);
    if (c.retain || c.selfRetain)kws.add(GameDictionary.RETAIN.NAMES[0]);
    if (c.purgeOnUse) kws.add("purge");
    if (c.exhaust || c.exhaustOnUseOnce) kws.add(GameDictionary.EXHAUST.NAMES[0]);

    // Equipment Traits.
    for (AbstractCardModifier mod : CardModifierManager.modifiers(c)) {
      if (!(mod instanceof EquipmentTrait)) continue;

      kws.add(((EquipmentTrait) mod).keyword);
    }

    return kws.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
  }

  private static void drawBadgeOnTip(float x, float y, SpriteBatch sb, Texture badge) {
    if(badge != null) {
      float badge_w = badge.getWidth();
      float badge_h = badge.getHeight();
      sb.draw(badge, x + ((320.0F - badge_w/2 - 8f) * Settings.scale), y + (-16.0F * Settings.scale), 0, 0, badge_w, badge_h,
              0.5f * Settings.scale, 0.5f * Settings.scale, 0, 0, 0, (int)badge_w, (int)badge_h, false, false);
    }
  }

  private static void DrawOnCardAuto(SpriteBatch sb, AbstractCard card, Texture img, Vector2 offset, float width, float height, Color color, float alpha, float scaleModifier) {
    if (card.angle != 0) offset.rotate(card.angle);

    offset.scl(Settings.scale * card.drawScale);
    DrawOnCardCentered(sb, card, new Color(color.r, color.g, color.b, alpha), img, card.current_x + offset.x, card.current_y + offset.y, width, height, scaleModifier);
  }

  private static void DrawOnCardCentered(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY, float width, float height, float scaleModifier) {
    final float scale = card.drawScale * Settings.scale * scaleModifier;

    sb.setColor(color);
    sb.draw(img, drawX - (width / 2f), drawY - (height / 2f), width / 2f, height / 2f, width, height,
            scale, scale, card.angle, 0, 0, img.getWidth(), img.getHeight(), false, false);
  }
}
