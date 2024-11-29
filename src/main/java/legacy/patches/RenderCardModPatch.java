package legacy.patches;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;
import legacy.cards.LegacyCard;
import legacy.cards.mods.ModifierWithBadge;
import legacy.cards.mods.SpellModifier;
import legacy.cards.mods.traits.*;
import legacy.util.TextureLoader;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

  private static final Map<String, Texture> BADGE_MAP;
  static {
    BADGE_MAP = new HashMap<>();
    // BASE GAME KEYWORDS.
    BADGE_MAP.put(GameDictionary.INNATE.NAMES[0].toLowerCase(), StSLib.BADGE_INNATE);
    BADGE_MAP.put(GameDictionary.EXHAUST.NAMES[0].toLowerCase(), StSLib.BADGE_EXHAUST);
    BADGE_MAP.put(GameDictionary.ETHEREAL.NAMES[0].toLowerCase(), StSLib.BADGE_ETHEREAL);
    BADGE_MAP.put(GameDictionary.RETAIN.NAMES[0].toLowerCase(), StSLib.BADGE_RETAIN);
    BADGE_MAP.put("purge", StSLib.BADGE_PURGE);

    // Custom Modifiers. We load textures directly here because of some funny problems with including textures on
    // the trait classes themselves. We run into problems loading them before the Gdx.files initialization happens.
    BADGE_MAP.put(FinesseTrait.ID, TextureLoader.getTexture("legacy/images/cards/mods/traits/finesse.png"));
    BADGE_MAP.put(FlurryTrait.ID, TextureLoader.getTexture("legacy/images/cards/mods/traits/flurry.png"));
    BADGE_MAP.put(HeavyArmorTrait.ID, TextureLoader.getTexture("legacy/images/cards/mods/traits/heavy_armor.png"));
    BADGE_MAP.put(LightArmorTrait.ID, TextureLoader.getTexture("legacy/images/cards/mods/traits/light_armor.png"));
    BADGE_MAP.put(MediumArmorTrait.ID, TextureLoader.getTexture("legacy/images/cards/mods/traits/medium_armor.png"));
    BADGE_MAP.put(RangedTrait.ID, TextureLoader.getTexture("legacy/images/cards/mods/traits/ranged.png"));
    BADGE_MAP.put(TwoHandedTrait.ID, TextureLoader.getTexture("legacy/images/cards/mods/traits/two_handed.png"));
    BADGE_MAP.put(SpellModifier.ID, TextureLoader.getTexture("legacy/images/cards/mods/traits/spell.png"));

    // Stat requirements.
    BADGE_MAP.put(LegacyCard.StatRequirements.STRENGTH, TextureLoader.getTexture("legacy/images/cards/mods/common/strength.png"));
    BADGE_MAP.put(LegacyCard.StatRequirements.DEXTERITY, TextureLoader.getTexture("legacy/images/cards/mods/common/dexterity.png"));
    BADGE_MAP.put(LegacyCard.StatRequirements.FOCUS, TextureLoader.getTexture("legacy/images/cards/mods/common/focus.png"));
  }

  @SpirePatch(clz=AbstractCard.class, method="renderCard")
  public static class RenderIcons {

    // Render badges in game.
    @SpireInsertPatch(locator=Locator.class)
    public static void patch(AbstractCard __instance, SpriteBatch sb, boolean hovered, boolean selected) {
      if (__instance instanceof LegacyCard) {
        renderBadges(sb, __instance);
      }
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

    @SpireInsertPatch(locator=Locator.class)
    public static void patch(AbstractCard instance, SpriteBatch sb) {
      if (instance instanceof LegacyCard) {
        renderBadges(sb, instance);
      }
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

      if (___card instanceof LegacyCard) {
        drawBadgeOnTip(x, y, sb, BADGE_MAP.get(word));
      }
    }
  }

  // Render in single card view madness.
  //
  // Hello past me and/or future me. I'm sure there was a reason why I needed to duplicate render logic here, but it's
  // been lost to time. I should probably figure this out eventually, but for now we just duplicate logic to make
  // things work. :shrug:
  @SpirePatch(clz=SingleCardViewPopup.class, method="render")
  public static class SingleCardViewRenderIconOnCard {

    @SpireInsertPatch(locator=Locator.class)
    public static void patch(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card, Hitbox ___cardHb) {
      if (___card instanceof LegacyCard) {
        int offsetY = 0;

        // BASE GAME MODIFIERS.
        if (___card.isInnate) offsetY += drawBadge(sb, ___card, ___cardHb, StSLib.BADGE_INNATE, offsetY);
        if (___card.isEthereal) offsetY += drawBadge(sb, ___card, ___cardHb, StSLib.BADGE_ETHEREAL, offsetY);
        if (___card.retain || ___card.selfRetain)
          offsetY += drawBadge(sb, ___card, ___cardHb, StSLib.BADGE_RETAIN, offsetY);
        if (___card.purgeOnUse) offsetY += drawBadge(sb, ___card, ___cardHb, StSLib.BADGE_PURGE, offsetY);
        if (___card.exhaust || ___card.exhaustOnUseOnce)
          offsetY += drawBadge(sb, ___card, ___cardHb, StSLib.BADGE_EXHAUST, offsetY);

        // EQUIPMENT TRAITS.
        for (AbstractCardModifier mod : CardModifierManager.modifiers(___card)) {
          if (!(mod instanceof ModifierWithBadge)) continue;

          offsetY += drawBadge(sb, ___card, ___cardHb, BADGE_MAP.get(((ModifierWithBadge) mod).id), offsetY);
        }

        // Stat requirements.
        LegacyCard.StatRequirements requirements = ((LegacyCard) ___card).statRequirements;

        offsetY = 0;
        if (requirements.strength > 0) {
          offsetY -= drawStatRequirement(sb, ___card, ___cardHb, BADGE_MAP.get(LegacyCard.StatRequirements.STRENGTH), offsetY, requirements.strength, new Color(0.8f, 0.5f, 0.5f, 1f));
        }
        if (requirements.dexterity > 0) {
          offsetY -= drawStatRequirement(sb, ___card, ___cardHb, BADGE_MAP.get(LegacyCard.StatRequirements.DEXTERITY), offsetY, requirements.dexterity, new Color(0.45f, 0.7f, 0.55f, 1f));
        }
        if (requirements.focus > 0) {
          offsetY -= drawStatRequirement(sb, ___card, ___cardHb, BADGE_MAP.get(LegacyCard.StatRequirements.FOCUS), offsetY, requirements.focus, new Color(0.45f, 0.55f, 0.8f, 1f));
        }
      }
    }

    private static int drawBadge(SpriteBatch sb, AbstractCard card, Hitbox hb, Texture img, int offset) {
      float badge_w = img.getWidth();
      float badge_h = img.getHeight();
      sb.draw(img, hb.x + hb.width - (badge_w * Settings.scale) * 0.66f, hb.y + hb.height - (badge_h * Settings.scale) * 0.5f - ((offset * (badge_h * Settings.scale)*0.6f)), 0, 0, badge_w , badge_h ,
              Settings.scale, Settings.scale, card.angle, 0, 0, (int)badge_w, (int)badge_h, false, false);
      return 1;
    }

    private static int drawStatRequirement(SpriteBatch sb, AbstractCard card, Hitbox hb, Texture texture, int offset, int value, Color backgroundColor) {
      final Texture circle = TextureLoader.getTexture("legacy/images/ui/common/circle.png");
      float targetX = hb.x;
      float targetY = hb.y + hb.height - (hb.height * 0.07f) - (circle.getHeight() * Settings.scale) + ((offset * (circle.getHeight() * Settings.scale) * 0.62f));

      DrawOnCardNoScale(sb, card, circle, new Vector2(targetX, targetY), circle.getWidth(), circle.getHeight(), backgroundColor, 1, Settings.scale);
      DrawOnCardNoScale(sb, card, texture, new Vector2(targetX, targetY), texture.getWidth(), texture.getHeight(), Color.WHITE, 1, Settings.scale * 1.2f);

      float textOffsetX = (circle.getWidth() * Settings.scale) / 8;
      float textOffsetY = - (circle.getHeight() * Settings.scale) / 8 - 4;
      FontHelper.cardDescFont_L.getData().setScale(card.drawScale * 1.6f);
      WriteOnCardAbsolute(sb, card, FontHelper.cardDescFont_L, "x" + value, targetX + textOffsetX, targetY + textOffsetY, Settings.CREAM_COLOR, true);
      FontHelper.cardDescFont_L.getData().setScale(1);

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
      drawBadgeOnTip(x, y, sb, BADGE_MAP.get(tip.header.toLowerCase()));

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
   * 3. Stat requirements (str, dex, focus).
   */
  private static void renderBadges(SpriteBatch sb, AbstractCard card) {
    final float alpha = card.transparency;
    int offsetY = 0;

    // BASE GAME MODIFIERS.
    if (card.isInnate) offsetY -= renderBadge(sb, card, StSLib.BADGE_INNATE, offsetY, alpha);
    if (card.isEthereal) offsetY -= renderBadge(sb, card, StSLib.BADGE_ETHEREAL, offsetY, alpha);
    if (card.retain || card.selfRetain) offsetY -= renderBadge(sb, card, StSLib.BADGE_RETAIN, offsetY, alpha);
    if (card.purgeOnUse) offsetY -= renderBadge(sb, card, StSLib.BADGE_PURGE, offsetY, alpha);
    if (card.exhaust || card.exhaustOnUseOnce) offsetY -= renderBadge(sb, card, StSLib.BADGE_EXHAUST, offsetY, alpha);

    // EQUIPMENT TRAITS.
    for (AbstractCardModifier mod : CardModifierManager.modifiers(card)) {
      if (!(mod instanceof ModifierWithBadge)) continue;

      offsetY -= renderBadge(sb, card, BADGE_MAP.get(((ModifierWithBadge) mod).id), offsetY, alpha);
    }

    // Stat requirements.
    if (!(card instanceof LegacyCard)) return;
    LegacyCard.StatRequirements requirements = ((LegacyCard) card).statRequirements;

    offsetY = 0;

    if (requirements.strength > 0) {
      offsetY -= renderStatRequirement(sb, card,BADGE_MAP.get(LegacyCard.StatRequirements.STRENGTH), offsetY, requirements.strength, new Color(0.8f, 0.5f, 0.5f, 1f));
    }
    if (requirements.dexterity > 0) {
      offsetY -= renderStatRequirement(sb, card,BADGE_MAP.get(LegacyCard.StatRequirements.DEXTERITY), offsetY, requirements.dexterity, new Color(0.45f, 0.7f, 0.55f, 1f));
    }
    if (requirements.focus > 0) {
      offsetY -= renderStatRequirement(sb, card,BADGE_MAP.get(LegacyCard.StatRequirements.FOCUS), offsetY, requirements.focus, new Color(0.45f, 0.55f, 0.8f, 1f));
    }

  }

  private static float renderBadge(SpriteBatch sb, AbstractCard card, Texture texture, float offset_y, float alpha) {
    Vector2 offset = new Vector2(AbstractCard.RAW_W * 0.45f, AbstractCard.RAW_H * 0.45f + offset_y);
    DrawOnCardAuto(sb, card, texture, offset, 64, 64, Color.WHITE, alpha, 1);
    return 38;
  }

  private static int renderStatRequirement(SpriteBatch sb, AbstractCard card, Texture texture, float offsetY, int value, Color backgroundColor) {
    final float offset_x = -AbstractCard.RAW_W * 0.46f;
    final float offset_y = AbstractCard.RAW_H * 0.275f;

    DrawOnCardAuto(sb, card, TextureLoader.getTexture("legacy/images/ui/common/circle.png"), new Vector2(offset_x, offset_y + offsetY),32, 32, backgroundColor, 1, 1);
    DrawOnCardAuto(sb, card, texture, new Vector2(offset_x, offset_y + offsetY), 24, 24, Color.WHITE, 1, 1);

    FontHelper.cardDescFont_L.getData().setScale(0.7f * card.drawScale);
    WriteOnCard(sb, card, FontHelper.cardDescFont_L, "x" + value, (offset_x + 6), (offset_y + offsetY - 12), Settings.CREAM_COLOR, true);
    FontHelper.cardDescFont_L.getData().setScale(1);

    return 35;
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
      if (!(mod instanceof ModifierWithBadge)) continue;

      kws.add(((ModifierWithBadge) mod).id);
    }

    return kws.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
  }

  private static void drawBadgeOnTip(float x, float y, SpriteBatch sb, Texture badge) {
    if (badge == null) return;

    float badge_w = badge.getWidth();
    float badge_h = badge.getHeight();
    sb.draw(badge, x + ((320.0F - badge_w/2 - 8f) * Settings.scale), y + (-16.0F * Settings.scale), 0, 0, badge_w, badge_h,
            0.5f * Settings.scale, 0.5f * Settings.scale, 0, 0, 0, (int)badge_w, (int)badge_h, false, false);
  }

  // 100% stolen from STS-Animator. Whatever they wrote both works and is above my pay grade, so we are taking that shit.
  public static void WriteOnCard(SpriteBatch sb, AbstractCard card, BitmapFont font, String text, float x, float y, Color color, boolean roundY) {
    final float scale = card.drawScale * Settings.scale;

    FontHelper.renderRotatedText(sb, font, text, card.current_x, card.current_y, x * scale, y * scale, card.angle, roundY, color);
  }

  public static void WriteOnCardAbsolute(SpriteBatch sb, AbstractCard card, BitmapFont font, String text, float x, float y, Color color, boolean roundY) {
    FontHelper.renderRotatedText(sb, font, text, x, y, 0, 0, card.angle, roundY, color);
  }

  private static void DrawOnCardAuto(SpriteBatch sb, AbstractCard card, Texture img, Vector2 offset, float width, float height, Color color, float alpha, float scaleModifier) {
    if (card.angle != 0) offset.rotate(card.angle);

    offset.scl(Settings.scale * card.drawScale);
    DrawOnCardCentered(sb, card, new Color(color.r, color.g, color.b, alpha), img, card.current_x + offset.x, card.current_y + offset.y, width, height, scaleModifier);
  }

  private static void DrawOnCardNoScale(SpriteBatch sb, AbstractCard card, Texture img, Vector2 offset, float width, float height, Color color, float alpha, float scaleModifier) {
    if (card.angle != 0) offset.rotate(card.angle);

    DrawOnCardCentered(sb, card, new Color(color.r, color.g, color.b, alpha), img, card.current_x + offset.x, card.current_y + offset.y, width, height, scaleModifier);
  }

  private static void DrawOnCardCentered(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY, float width, float height, float scaleModifier) {
    final float scale = card.drawScale * Settings.scale * scaleModifier;

    sb.setColor(color);
    sb.draw(img, drawX - (width / 2f), drawY - (height / 2f), width / 2f, height / 2f, width, height, scale, scale, card.angle, 0, 0, img.getWidth(), img.getHeight(), false, false);
  }
}
