package legacy.characters;

import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import legacy.cards.armor.PaddedArmor;
import legacy.cards.weapons.Anathema;
import legacy.cards.weapons.Rapier;
import legacy.util.MonsterUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import legacy.LegacyMod;
import legacy.relics.DefaultClickableRelic;
import legacy.relics.PlaceholderRelic;
import legacy.relics.PlaceholderRelic2;

import java.io.IOException;
import java.util.ArrayList;

import static legacy.LegacyMod.*;
import static legacy.characters.TheAdventurer.Enums.COLOR_GRAY;

public class TheAdventurer extends CustomPlayer {
    public static final Logger logger = LogManager.getLogger(LegacyMod.class.getName());

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_DEFAULT;
        @SpireEnum(name = "DEFAULT_GRAY_COLOR") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor COLOR_GRAY;
        @SpireEnum(name = "DEFAULT_GRAY_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    // =============== CHARACTER ENUMERATORS  =================


    // =============== BASE STATS =================

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_GOLD = 0;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    // =============== /BASE STATS/ =================

    // RPG STATS

    public int level;
    public int xp;
    public int fighterLevel;
    public int rogueLevel;
    public int wizardLevel;


    // =============== STRINGS =================

    private static final String ID = makeID("DefaultCharacter");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    // =============== /STRINGS/ =================


    // =============== TEXTURES OF BIG ENERGY ORB ===============

    public static final String[] orbTextures = {
            "legacy/images/char/defaultCharacter/orb/layer1.png",
            "legacy/images/char/defaultCharacter/orb/layer2.png",
            "legacy/images/char/defaultCharacter/orb/layer3.png",
            "legacy/images/char/defaultCharacter/orb/layer4.png",
            "legacy/images/char/defaultCharacter/orb/layer5.png",
            "legacy/images/char/defaultCharacter/orb/layer6.png",
            "legacy/images/char/defaultCharacter/orb/layer1d.png",
            "legacy/images/char/defaultCharacter/orb/layer2d.png",
            "legacy/images/char/defaultCharacter/orb/layer3d.png",
            "legacy/images/char/defaultCharacter/orb/layer4d.png",
            "legacy/images/char/defaultCharacter/orb/layer5d.png",};

    // =============== /TEXTURES OF BIG ENERGY ORB/ ===============

    // =============== CHARACTER CLASS START =================

    public TheAdventurer(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures,
                "legacy/images/char/defaultCharacter/orb/vfx.png", null,
                new SpriterAnimation(
                        "legacy/images/char/defaultCharacter/Spriter/theDefaultAnimation.scml"));


        // =============== TEXTURES, ENERGY, LOADOUT =================  

        initializeClass(null, // required call to load textures and setup energy/loadout.
                // I left these in DefaultMod.java (Ctrl+click them to see where they are, Ctrl+hover to see what they read.)
                THE_DEFAULT_SHOULDER_2, // campfire pose
                THE_DEFAULT_SHOULDER_1, // another campfire pose
                THE_DEFAULT_CORPSE, // dead corpse
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        // =============== /TEXTURES, ENERGY, LOADOUT/ =================


        // =============== ANIMATIONS =================  

        loadAnimation(
                THE_DEFAULT_SKELETON_ATLAS,
                THE_DEFAULT_SKELETON_JSON,
                1.0f);
        AnimationState.TrackEntry e = state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        // =============== /ANIMATIONS/ =================


        // =============== TEXT BUBBLE LOCATION =================

        dialogX = (drawX + 0.0F * Settings.scale); // set location for text bubbles
        dialogY = (drawY + 220.0F * Settings.scale); // you can just copy these values

        // =============== /TEXT BUBBLE LOCATION/ =================

        // Load RPG Stats
        this.level = CHARACTER_STATS.getInt("level");
        this.xp = CHARACTER_STATS.getInt("xp");
        this.fighterLevel = CHARACTER_STATS.getInt("fighter_level");
        this.rogueLevel = CHARACTER_STATS.getInt("rogue_level");
        this.wizardLevel = CHARACTER_STATS.getInt("wizard_level");
    }

    // =============== /CHARACTER CLASS END/ =================

    // Starting description and loadout
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                getMaxHp(), getMaxHp(), ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    // Max HP is a base of 35 + 2hp/lvl.
    public static int getMaxHp() {
        return CHARACTER_STATS.getInt("level") * 2 + 35;
    }

    public void gainXp(AbstractMonster m) {
        this.xp += MonsterUtils.getXp(m);
    }

    public void commitStats() {
        CHARACTER_STATS.setInt("level", this.level);
        CHARACTER_STATS.setInt("xp", this.xp);
        CHARACTER_STATS.setInt("fighter_level", this.fighterLevel);
        CHARACTER_STATS.setInt("rogue_level", this.rogueLevel);
        CHARACTER_STATS.setInt("wizard_level", this.wizardLevel);
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> deck = new ArrayList<>();

        deck.add(Anathema.ID);
        deck.add(Rapier.ID);
        deck.add(Rapier.ID);
        deck.add(PaddedArmor.ID);
        deck.add(PaddedArmor.ID);

        return deck;
    }

    // Starting Relics	
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(PlaceholderRelic.ID);
        retVal.add(PlaceholderRelic2.ID);
        retVal.add(DefaultClickableRelic.ID);

        UnlockTracker.markRelicAsSeen(PlaceholderRelic.ID);
        UnlockTracker.markRelicAsSeen(PlaceholderRelic2.ID);
        UnlockTracker.markRelicAsSeen(DefaultClickableRelic.ID);

        return retVal;
    }

    // character Select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_DAGGER_1", 1.25f); // Sound Effect
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false); // Screen Effect
    }

    // character Select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DAGGER_1";
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
    @Override
    public int getAscensionMaxHPLoss() {
        return 0;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return COLOR_GRAY;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return LegacyMod.DEFAULT_GRAY;
    }

    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    //Which card should be obtainable from the Match and Keep event?
    @Override
    public AbstractCard getStartCardForEvent() {
        return new Anathema();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new TheAdventurer(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return LegacyMod.DEFAULT_GRAY;
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return LegacyMod.DEFAULT_GRAY;
    }

    // Should return an AttackEffect array of any size greater than 0. These effects
    // will be played in sequence as your character's finishing combo on the heart.
    // Attack effects are the same as used in DamageAction and the like.
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY};
    }

    // Should return a string containing what text is shown when your character is
    // about to attack the heart. For example, the defect is "NL You charge your
    // core to its maximum..."
    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    // The vampire events refer to the base game characters as "brother", "sister",
    // and "broken one" respectively.This method should return a String containing
    // the full text that will be displayed as the first screen of the vampires event.
    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    // We need to commit changes to the database either when a fight is won OR when a player dies.
    // This is to avoid cases where permanent DB changes get committed during a fight, allowing the player
    // to save and quit while still retaining permanent buffs.
    @Override
    public void onVictory() {
        super.onVictory();

        commitStats();
        LegacyMod.saveCharacterStats();
        LEGACY_DB.commitChanges();
    }

    // I don't want to write a really ugly spire patch in order to make this work, so instead we slap some logic
    // into the playDeathAnimation function().
    //
    // I'm sorry programming gods for my insolence, but I just want it to work.
    @Override
    public void playDeathAnimation() {
        super.playDeathAnimation();

        commitStats();
        LegacyMod.saveCharacterStats();
        LEGACY_DB.commitChanges();
    }



}
