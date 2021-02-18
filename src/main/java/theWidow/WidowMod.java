package theWidow;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.DynamicVariable;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.mod.widepotions.WidePotionsMod;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.BlessingOfTheForge;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.relics.SacredBark;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theWidow.cards.BombLauncher;
import theWidow.cards.Chemistry;
import theWidow.cards.ExtraMagicalCustomCard;
import theWidow.potions.*;
import theWidow.powers.WebPower;
import theWidow.relics.BlackBoxRelic;
import theWidow.relics.CyberheartRelic;
import theWidow.relics.HourglassMarkRelic;
import theWidow.util.TextureLoader;
import theWidow.variables.UpgradesInHand;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SpireInitializer
public class WidowMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber,
        OnPowersModifiedSubscriber,
        PostPotionUseSubscriber,
        PostPowerApplySubscriber,
        AddAudioSubscriber,
        PostBattleSubscriber{
    public static final Logger logger = LogManager.getLogger(WidowMod.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties theWidowSettings = new Properties();
    public static final String WEB_INTENT_SETTINGS = "webIntent";
    public static boolean webAffectsIntents = false;

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "The Widow";
    private static final String AUTHOR = "wavewhyse";
    private static final String DESCRIPTION = "spider but also cyborg";
    
    // =============== INPUT TEXTURE LOCATION =================
    // Colors (RGB)
    // Character Color
    public static final Color WIDOW_BLACK = CardHelper.getColor(55, 0, 20);
    public static final String THE_DEFAULT_SHOULDER_1 = "theWidowResources/images/char/defaultCharacter/shoulder.png";
    public static final String THE_DEFAULT_SHOULDER_2 = "theWidowResources/images/char/defaultCharacter/shoulder2.png";
    public static final String THE_WIDOW_CORPSE = "theWidowResources/images/char/widowCharacter/corpse.png";
    // Atlas and JSON files for the Animations
    public static final String THE_WIDOW_SKELETON_ATLAS = "theWidowResources/images/char/widowCharacter/Widow.atlas";
    public static final String THE_WIDOW_SKELETON_JSON = "theWidowResources/images/char/widowCharacter/Widow.json";
    // Card backgrounds - The actual rectangular card.
    static final String ATTACK_WIDOW_BLACK = "theWidowResources/images/512/bg_attack_widow_black.png";
    static final String SKILL_WIDOW_BLACK = "theWidowResources/images/512/bg_skill_widow_black.png";
    static final String POWER_WIDOW_BLACK = "theWidowResources/images/512/bg_power_widow_black.png";
    static final String ENERGY_ORB_WIDOW_BLACK = "theWidowResources/images/512/card_widow_black_orb2.png";
    static final String CARD_ENERGY_ORB = "theWidowResources/images/512/card_small_orb2.png";
    static final String ATTACK_WIDOW_BLACK_PORTRAIT = "theWidowResources/images/1024/bg_attack_widow_black.png";
    static final String SKILL_WIDOW_BLACK_PORTRAIT = "theWidowResources/images/1024/bg_skill_widow_black.png";
    static final String POWER_WIDOW_BLACK_PORTRAIT = "theWidowResources/images/1024/bg_power_widow_black.png";
    static final String ENERGY_ORB_WIDOW_BLACK_PORTRAIT = "theWidowResources/images/1024/card_widow_black_orb2.png";
    // Character assets
    static final String THE_WIDOW_BUTTON = "theWidowResources/images/charSelect/WidowCharacterButton.png";
    static final String THE_WIDOW_PORTRAIT = "theWidowResources/images/charSelect/WidowCharacterPortraitBG.png";

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "theWidowResources/images/Badge.png";

    public static int EGG_CLUTCH_UPGRADES = 0;

    // =============== MAKE IMAGE PATHS =================
    
    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }
    
    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }
    
    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }
    
    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }
    
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public WidowMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);

        setModID("theWidow");
        
        logger.info("Done subscribing");
        
        logger.info("Creating the color " + TheWidow.Enums.COLOR_BLACK.toString());

        BaseMod.addColor(TheWidow.Enums.COLOR_BLACK, WIDOW_BLACK, WIDOW_BLACK, WIDOW_BLACK,
                WIDOW_BLACK, WIDOW_BLACK, WIDOW_BLACK, WIDOW_BLACK,
                ATTACK_WIDOW_BLACK, SKILL_WIDOW_BLACK, POWER_WIDOW_BLACK, ENERGY_ORB_WIDOW_BLACK,
                ATTACK_WIDOW_BLACK_PORTRAIT, SKILL_WIDOW_BLACK_PORTRAIT, POWER_WIDOW_BLACK_PORTRAIT,
                ENERGY_ORB_WIDOW_BLACK_PORTRAIT, CARD_ENERGY_ORB);
        
        logger.info("Done creating the color");
        
        
        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        theWidowSettings.setProperty(WEB_INTENT_SETTINGS, "FALSE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig("widowMod", "theWidowConfig", theWidowSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            webAffectsIntents = config.getBool(WEB_INTENT_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");
        
    }
    
    public static void setModID(String ID) {
        modID = ID;
    }
    
    public static String getModID() {
        return modID;
    }
    
    private static void pathCheck() {
    }
    
    
    public static void initialize() {
        logger.info("========================= Initializing The Widow Mod. Hi. =========================");
        WidowMod widowMod = new WidowMod();
        logger.info("========================= /The Widow Mod Initialized. Hello World./ =========================");
    }
    
    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================

    // =============== LOAD THE CHARACTER =================
    
    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + TheWidow.Enums.THE_WIDOW.toString());
        
        BaseMod.addCharacter(new TheWidow("the Widow", TheWidow.Enums.THE_WIDOW),
                THE_WIDOW_BUTTON, THE_WIDOW_PORTRAIT, TheWidow.Enums.THE_WIDOW);
        
        receiveEditPotions();
        logger.info("Added " + TheWidow.Enums.THE_WIDOW.toString());
    }
    
    // =============== /LOAD THE CHARACTER/ =================
    
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        
        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("Web affects enemy intents (shows incorrect numbers when you don't have enough Web)",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                webAffectsIntents, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
            
            webAffectsIntents = button.enabled; // The boolean true/false will be whether the button is enabled or not
            try {
                // And based on that boolean, set the settings and save them
                SpireConfig config = new SpireConfig("widowMod", "theWidowConfig", theWidowSettings);
                config.setBool(WEB_INTENT_SETTINGS, webAffectsIntents);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");

        if (Loader.isModLoaded("widepotions")) {
            WidePotionsMod.whitelistSimplePotion(SilkPotion.POTION_ID);
            WidePotionsMod.whitelistSimplePotion(VenomCocktailPotion.POTION_ID);
            WidePotionsMod.whitelistSimplePotion(NeurostimulantPotion.POTION_ID);
        }
    }
    
    // =============== / POST-INITIALIZE/ =================
    
    // ================ ADD POTIONS ===================
    
    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        BaseMod.addPotion(SilkPotion.class, SilkPotion.LIQUID_COLOR, SilkPotion.HYBRID_COLOR, SilkPotion.SPOTS_COLOR, SilkPotion.POTION_ID, TheWidow.Enums.THE_WIDOW);
        BaseMod.addPotion(NeurostimulantPotion.class, NeurostimulantPotion.LIQUID_COLOR, NeurostimulantPotion.HYBRID_COLOR, NeurostimulantPotion.SPOTS_COLOR, NeurostimulantPotion.POTION_ID, TheWidow.Enums.THE_WIDOW);
        BaseMod.addPotion(VenomCocktailPotion.class, VenomCocktailPotion.LIQUID_COLOR, VenomCocktailPotion.HYBRID_COLOR, VenomCocktailPotion.SPOTS_COLOR, VenomCocktailPotion.POTION_ID, TheWidow.Enums.THE_WIDOW);

        BaseMod.addPotion(GrenadePotion.class, GrenadePotion.LIQUID_COLOR, GrenadePotion.HYBRID_COLOR, GrenadePotion.SPOTS_COLOR, GrenadePotion.POTION_ID, TheWidow.Enums.THE_WIDOW);
        BaseMod.addPotion(PulseBombPotion.class, PulseBombPotion.LIQUID_COLOR, PulseBombPotion.HYBRID_COLOR, PulseBombPotion.SPOTS_COLOR, PulseBombPotion.POTION_ID, TheWidow.Enums.THE_WIDOW);
        BaseMod.addPotion(MiniNukePotion.class, MiniNukePotion.LIQUID_COLOR, MiniNukePotion.HYBRID_COLOR, MiniNukePotion.SPOTS_COLOR, MiniNukePotion.POTION_ID, TheWidow.Enums.THE_WIDOW);
        BaseMod.addPotion(DistilledCardPotion.class, DistilledCardPotion.LIQUID_COLOR, DistilledCardPotion.HYBRID_COLOR, DistilledCardPotion.SPOTS_COLOR, DistilledCardPotion.POTION_ID, TheWidow.Enums.THE_WIDOW);
        
        logger.info("Done editing potions");
    }
    
    // ================ /ADD POTIONS/ ===================
    
    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        new AutoAdd("TheWidowMod")
                .packageFilter(CyberheartRelic.class)
                .setDefaultSeen(true)
                .any(CustomRelic.class, (info, relic) -> {
                    BaseMod.addRelicToCustomPool(relic, TheWidow.Enums.COLOR_BLACK);
                });

        logger.info("Done adding relics!");
    }
    
    // ================ /ADD RELICS/ ===================
    
    
    // ================ ADD CARDS ===================
    
    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variables");
        // Add the Custom Dynamic variables
        new AutoAdd("TheWidowMod")
                .packageFilter(UpgradesInHand.class)
                .any(DynamicVariable.class, (info, dynamicVariable) -> {
                    BaseMod.addDynamicVariable(dynamicVariable);
                });
        
        logger.info("Adding cards");

        new AutoAdd("TheWidowMod") // ${project.artifactId}
                .packageFilter(ExtraMagicalCustomCard.class) // filters to any class in the same package as ExtraMagicalCustomCard, nested packages included
                .setDefaultSeen(true)
                .cards();

        logger.info("Done adding cards!");
    }
    
    // ================ /ADD CARDS/ ===================
    
    
    // ================ LOAD THE TEXT ===================
    
    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + getModID());
        
        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/cardstrings.json");
        
        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/powerstrings.json");
        
        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/relicstrings.json");
        
        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/eng/potionstrings.json");

        // UIStrings
        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/eng/uistrings.json");
        
        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/eng/characterstrings.json");
        
        logger.info("Done editing strings");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword
        
        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/keywordstrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("HammerHit", getModID() + "Resources/sounds/hammer.ogg");
        BaseMod.addAudio("IN_FOR_THE_KILL", getModID() + "Resources/sounds/kill.ogg");
    }
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    @Override
    public void receivePowersModified() {
        if (AbstractDungeon.player.hasRelic(HourglassMarkRelic.ID))
            AbstractDungeon.player.getRelic(HourglassMarkRelic.ID).onTrigger();
    }

    @Override
    public void receivePostPotionUse(AbstractPotion pot) {
        if (AbstractDungeon.player.hasPower(BombLauncher.BombLauncherPower.POWER_ID)) {
            AbstractCreature target = AbstractDungeon.getRandomMonster();
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                if (m.hb.hovered && !m.isDying)
                    target = m;
            ((BombLauncher.BombLauncherPower) AbstractDungeon.player.getPower(BombLauncher.BombLauncherPower.POWER_ID)).onPotionUse(pot, target);
            if (AbstractDungeon.player.hasRelic(SacredBark.ID) && pot instanceof BlessingOfTheForge)
                pot.use(AbstractDungeon.player);
            if (AbstractDungeon.player.hasPower(Chemistry.ChemistryPower.POWER_ID))
                ((Chemistry.ChemistryPower)AbstractDungeon.player.getPower(Chemistry.ChemistryPower.POWER_ID)).onPotionUse(pot);
        }
        if (AbstractDungeon.player.hasRelic(SacredBark.ID) && pot instanceof BlessingOfTheForge)
            pot.use(AbstractDungeon.player);
        if (AbstractDungeon.player.hasPower(Chemistry.ChemistryPower.POWER_ID))
            ((Chemistry.ChemistryPower) AbstractDungeon.player.getPower(Chemistry.ChemistryPower.POWER_ID)).onPotionUse(pot);
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                if (AbstractDungeon.player.hasPower(WebPower.POWER_ID))
                    ((WebPower) AbstractDungeon.player.getPower(WebPower.POWER_ID)).updateWebbedMonsters();
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                    m.applyPowers();
                isDone = true;
            }
        });
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (AbstractDungeon.player.hasRelic(BlackBoxRelic.ID) && power instanceof ArtifactPower && target instanceof AbstractMonster)
            ((BlackBoxRelic) AbstractDungeon.player.getRelic(BlackBoxRelic.ID)).onTrigger(power, target);
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        for (RewardItem r : abstractRoom.rewards)
            if (r.cards != null)
                for (AbstractCard c : r.cards)
                    c.upgrade();
    }
}
