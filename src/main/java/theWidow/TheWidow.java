package theWidow;

import basemod.abstracts.CustomPlayer;
import basemod.animations.SpineAnimation;
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
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theWidow.cards.DefendWidow;
import theWidow.cards.Steelweave;
import theWidow.cards.StrikeWidow;
import theWidow.cards.StringShot;
import theWidow.relics.CyberheartRelic;

import java.util.ArrayList;

import static theWidow.WidowMod.makeID;

public class TheWidow extends CustomPlayer {
    public static final Logger logger = LogManager.getLogger(WidowMod.class.getName());

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_WIDOW;
        @SpireEnum(name = "THE_WIDOW")
        public static AbstractCard.CardColor COLOR_BLACK;
        @SpireEnum(name = "THE_WIDOW") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType WIDOW_BLACK;
        @SpireEnum
        public static AbstractPotion.PotionRarity BOMB;
    }

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 75;
    public static final int MAX_HP = 75;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    private static final String ID = makeID("WidowCharacter");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    public static final String[] orbTextures = {
            "theWidowResources/images/char/defaultCharacter/orb/layer1.png",
            "theWidowResources/images/char/defaultCharacter/orb/layer2.png",
            "theWidowResources/images/char/defaultCharacter/orb/layer3.png",
            "theWidowResources/images/char/defaultCharacter/orb/layer4.png",
            "theWidowResources/images/char/defaultCharacter/orb/layer5.png",
            "theWidowResources/images/char/defaultCharacter/orb/layer6.png",
            "theWidowResources/images/char/defaultCharacter/orb/layer1d.png",
            "theWidowResources/images/char/defaultCharacter/orb/layer2d.png",
            "theWidowResources/images/char/defaultCharacter/orb/layer3d.png",
            "theWidowResources/images/char/defaultCharacter/orb/layer4d.png",
            "theWidowResources/images/char/defaultCharacter/orb/layer5d.png",};

    public TheWidow(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures,
                "theWidowResources/images/char/defaultCharacter/orb/vfx.png", null,
                new SpineAnimation(WidowMod.THE_WIDOW_SKELETON_ATLAS, WidowMod.THE_WIDOW_SKELETON_JSON, 1f));
                //new SpriterAnimation(
                //        "theWidowResources/images/char/widowCharacter/Spriter/theWidowAnimation.scml"));


        // =============== TEXTURES, ENERGY, LOADOUT =================  

        initializeClass(null, // required call to load textures and setup energy/loadout.
                WidowMod.THE_DEFAULT_SHOULDER_2, // campfire pose
                WidowMod.THE_DEFAULT_SHOULDER_1, // another campfire pose
                WidowMod.THE_WIDOW_CORPSE, // dead corpse
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        // =============== /TEXTURES, ENERGY, LOADOUT/ =================


        // =============== ANIMATIONS =================  

        loadAnimation(
                WidowMod.THE_WIDOW_SKELETON_ATLAS,
                WidowMod.THE_WIDOW_SKELETON_JSON,
                1.0f);
        AnimationState.TrackEntry e = state.setAnimation(0, "animtion0", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        // =============== /ANIMATIONS/ =================


        // =============== TEXT BUBBLE LOCATION =================

        dialogX = (drawX + 0.0F * Settings.scale); // set location for text bubbles
        dialogY = (drawY + 220.0F * Settings.scale); // you can just copy these values

        // =============== /TEXT BUBBLE LOCATION/ =================

    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        logger.info("Begin loading starter Deck Strings");

        retVal.add(StrikeWidow.ID);
        retVal.add(StrikeWidow.ID);
        retVal.add(StrikeWidow.ID);
        retVal.add(StrikeWidow.ID);

        retVal.add(DefendWidow.ID);
        retVal.add(DefendWidow.ID);
        retVal.add(DefendWidow.ID);
        retVal.add(DefendWidow.ID);

        retVal.add(Steelweave.ID);
        retVal.add(StringShot.ID);

        return retVal;
    }

    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(CyberheartRelic.ID);

        UnlockTracker.markRelicAsSeen(CyberheartRelic.ID);

        return retVal;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("MONSTER_SNECKO_GLARE", 1.25f); // Sound Effect
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false); // Screen Effect
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "MONSTER_SNECKO_GLARE";
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 4;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return Enums.COLOR_BLACK;
    }

    @Override
    public Color getCardTrailColor() {
        return WidowMod.WIDOW_BLACK.cpy();
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontPurple;
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Steelweave();
    }

    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new TheWidow(name, chosenClass);
    }

    @Override
    public Color getCardRenderColor() {
        return WidowMod.WIDOW_BLACK;
    }

    @Override
    public Color getSlashAttackColor() {
        return WidowMod.WIDOW_BLACK;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                AbstractGameAction.AttackEffect.POISON};
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    @Override
    public String getSensoryStoneText() {
        return TEXT[3];
    }
}
