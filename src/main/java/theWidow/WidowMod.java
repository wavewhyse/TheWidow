package theWidow;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModPanel;
import basemod.abstracts.CustomCard;
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
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theWidow.cards.BetaCard;
import theWidow.cards.rare.BombLauncher;
import theWidow.cards.uncommon.Chemistry;
import theWidow.potions.NeurostimulantPotion;
import theWidow.potions.SilkPotion;
import theWidow.potions.VenomCocktailPotion;
import theWidow.relics.BlackBox;
import theWidow.relics.Cyberheart;
import theWidow.util.CardArtRoller;
import theWidow.util.TexLoader;
import theWidow.util.Wiz;
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
        PostPotionUseSubscriber,
        PostPowerApplySubscriber,
        AddAudioSubscriber{
    public static final Logger logger = LogManager.getLogger(WidowMod.class.getName());
    public static String modID;

    public static final Properties theWidowSettings = new Properties();
    public static final String BOTF_PATCH_SETTING = "BOTFPatched";
    public static boolean enableBOTFPatch = true;

    private static final String MODNAME = "The Widow";
    private static final String AUTHOR = "wavewhyse☆";
    private static final String DESCRIPTION = "spider but also cyborg";

    public static final Color WIDOW_BLACK = CardHelper.getColor(55, 0, 20);
    public static final String THE_DEFAULT_SHOULDER_1 = "theWidowResources/images/char/defaultCharacter/shoulder.png";
    public static final String THE_DEFAULT_SHOULDER_2 = "theWidowResources/images/char/defaultCharacter/shoulder2.png";
    public static final String THE_WIDOW_CORPSE = "theWidowResources/images/char/widowCharacter/corpse.png";
    public static final String THE_WIDOW_SKELETON_ATLAS = "theWidowResources/images/char/widowCharacter/Widow.atlas";
    public static final String THE_WIDOW_SKELETON_JSON = "theWidowResources/images/char/widowCharacter/Widow.json";
    static final String SKILL_WIDOW_BLACK = "theWidowResources/images/512/bg_skill_widow_black.png";
    static final String POWER_WIDOW_BLACK = "theWidowResources/images/512/bg_power_widow_black.png";
    static final String ENERGY_ORB_WIDOW_BLACK = "theWidowResources/images/512/card_widow_black_orb3.png";
    static final String CARD_ENERGY_ORB = "theWidowResources/images/512/card_small_black_orb3.png";
    static final String ATTACK_WIDOW_BLACK_PORTRAIT = "theWidowResources/images/1024/bg_attack_widow_black.png";
    static final String SKILL_WIDOW_BLACK_PORTRAIT = "theWidowResources/images/1024/bg_skill_widow_black.png";
    static final String POWER_WIDOW_BLACK_PORTRAIT = "theWidowResources/images/1024/bg_power_widow_black.png";
    static final String ENERGY_ORB_WIDOW_BLACK_PORTRAIT = "theWidowResources/images/1024/card_widow_black_orb3.png";
    static final String THE_WIDOW_BUTTON = "theWidowResources/images/charSelect/WidowCharacterButton.png";
    static final String THE_WIDOW_PORTRAIT = "theWidowResources/images/charSelect/WidowCharacterPortraitBG.png";

    public static final String BADGE_IMAGE = "theWidowResources/images/Badge.png";

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath + ".png";
    }

    public static String makeImagePath(String resourcePath) {
        return getModID() + "Resources/images/" + resourcePath + ".png";
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath + ".png";
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath + ".png";
    }
    
    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath + ".png";
    }

    public WidowMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);

        setModID("theWidow");
        
        logger.info("Done subscribing");
        
        logger.info("Creating the color " + TheWidow.Enums.COLOR_BLACK.toString());

        BaseMod.addColor(TheWidow.Enums.COLOR_BLACK, WIDOW_BLACK, WIDOW_BLACK, WIDOW_BLACK,
                WIDOW_BLACK, WIDOW_BLACK, WIDOW_BLACK, WIDOW_BLACK,
                "theWidowResources/images/512/bg_attack_widow_black.png", SKILL_WIDOW_BLACK, POWER_WIDOW_BLACK, ENERGY_ORB_WIDOW_BLACK,
                ATTACK_WIDOW_BLACK_PORTRAIT, SKILL_WIDOW_BLACK_PORTRAIT, POWER_WIDOW_BLACK_PORTRAIT,
                ENERGY_ORB_WIDOW_BLACK_PORTRAIT, CARD_ENERGY_ORB);
        
        logger.info("Done creating the color");
        
        
        logger.info("Adding mod settings");

        theWidowSettings.setProperty(BOTF_PATCH_SETTING, "TRUE");
        try {
            SpireConfig config = new SpireConfig("widowMod", "theWidowConfig", theWidowSettings);
            config.load();
            enableBOTFPatch = config.getBool(BOTF_PATCH_SETTING);
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

    public static void initialize() {
        logger.info("========================= Initializing The Widow Mod. hi =========================");
        WidowMod widowMod = new WidowMod();
        logger.info("========================= /The Widow Mod Initialized. hi/ =========================");
    }

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + TheWidow.Enums.THE_WIDOW.toString());
        
        BaseMod.addCharacter(new TheWidow("the Widow", TheWidow.Enums.THE_WIDOW),
                THE_WIDOW_BUTTON, THE_WIDOW_PORTRAIT, TheWidow.Enums.THE_WIDOW);
        
        receiveEditPotions();

        logger.info("Added " + TheWidow.Enums.THE_WIDOW.toString());
    }

    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        
        Texture badgeTexture = TexLoader.getTexture(BADGE_IMAGE);
        
        ModPanel settingsPanel = new ModPanel();
        
//        ModLabeledToggleButton enableBOTFPatchButton = new ModLabeledToggleButton("Enable Blessing of the Forge changes (may cause mod conflicts):\n   - Changes Blessing of the Forge to use Widow's Upgrade VFX, and also to be doubled with Sacred Bark.",
//                350.0f, 650.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
//                enableBOTFPatch,
//                settingsPanel,
//                (label) -> {},
//                (button) -> {
//
//            enableBOTFPatch = button.enabled;
//            try {
//                SpireConfig config = new SpireConfig("widowMod", "theWidowConfig", theWidowSettings);
//                config.setBool(BOTF_PATCH_SETTING, enableBOTFPatch);
//                config.save();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//
//        settingsPanel.addUIElement(enableBOTFPatchButton);
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        logger.info("Done loading badge Image and mod options");

        for (AbstractCard c : CardLibrary.getCardList(TheWidow.Enums.WIDOW_BLACK))
            if (c instanceof CustomCard && (((CustomCard)c).textureImg.contains("Attack.png") || ((CustomCard)c).textureImg.contains("Skill.png") || ((CustomCard)c).textureImg.contains("Power.png")))
                CardArtRoller.computeCard(c);

        if (Loader.isModLoaded("widepotions")) {
            WidePotionsMod.whitelistSimplePotion(SilkPotion.POTION_ID);
            WidePotionsMod.whitelistSimplePotion(VenomCocktailPotion.POTION_ID);
            WidePotionsMod.whitelistSimplePotion(NeurostimulantPotion.POTION_ID);
        }
    }

    public void receiveEditPotions() {
        logger.info("Adding potions");

        new AutoAdd("TheWidowMod")
                .packageFilter(SilkPotion.class)
                        .any(AbstractPotion.class, (info, potion) -> {
                            BaseMod.addPotion(potion.getClass(), potion.liquidColor, potion.hybridColor, potion.spotsColor, potion.ID, TheWidow.Enums.THE_WIDOW);
                        });

//        BaseMod.addPotion(SilkPotion.class, SilkPotion.LIQUID_COLOR, SilkPotion.HYBRID_COLOR, SilkPotion.SPOTS_COLOR, SilkPotion.POTION_ID, TheWidow.Enums.THE_WIDOW);
//        BaseMod.addPotion(NeurostimulantPotion.class, NeurostimulantPotion.LIQUID_COLOR, NeurostimulantPotion.HYBRID_COLOR, NeurostimulantPotion.SPOTS_COLOR, NeurostimulantPotion.POTION_ID, TheWidow.Enums.THE_WIDOW);
//        BaseMod.addPotion(VenomCocktailPotion.class, VenomCocktailPotion.LIQUID_COLOR, VenomCocktailPotion.HYBRID_COLOR, VenomCocktailPotion.SPOTS_COLOR, VenomCocktailPotion.POTION_ID, TheWidow.Enums.THE_WIDOW);
//
//        BaseMod.addPotion(GrenadePotion.class, GrenadePotion.LIQUID_COLOR, GrenadePotion.HYBRID_COLOR, GrenadePotion.SPOTS_COLOR, GrenadePotion.POTION_ID, TheWidow.Enums.THE_WIDOW);
//        BaseMod.addPotion(PulseBombPotion.class, PulseBombPotion.LIQUID_COLOR, PulseBombPotion.HYBRID_COLOR, PulseBombPotion.SPOTS_COLOR, PulseBombPotion.POTION_ID, TheWidow.Enums.THE_WIDOW);
//        BaseMod.addPotion(MiniNukePotion.class, MiniNukePotion.LIQUID_COLOR, MiniNukePotion.HYBRID_COLOR, MiniNukePotion.SPOTS_COLOR, MiniNukePotion.POTION_ID, TheWidow.Enums.THE_WIDOW);
//        BaseMod.addPotion(DistilledCardPotion.class, DistilledCardPotion.LIQUID_COLOR, DistilledCardPotion.HYBRID_COLOR, DistilledCardPotion.SPOTS_COLOR, DistilledCardPotion.POTION_ID, TheWidow.Enums.THE_WIDOW);
        
        logger.info("Done adding potions!");
    }

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        new AutoAdd("TheWidowMod")
                .packageFilter(Cyberheart.class)
                .any(CustomRelic.class, (info, relic) -> {
                    BaseMod.addRelicToCustomPool(relic, TheWidow.Enums.COLOR_BLACK);
                    if (!info.seen)
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                });

        logger.info("Done adding relics!");
    }

    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        new AutoAdd("TheWidowMod")
                .packageFilter(UpgradesInHand.class)
                .any(DynamicVariable.class, (info, dynamicVariable) -> {
                    BaseMod.addDynamicVariable(dynamicVariable);
                });
        logger.info("Done adding variables.");
        logger.info("Adding cards");

        new AutoAdd("TheWidowMod")
                .packageFilter(BetaCard.class)
                .setDefaultSeen(true)
                .cards();

        logger.info("Done adding cards!");
    }

    @Override
    public void receiveEditStrings() {
        logger.info("Adding strings");

        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/cardstrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/powerstrings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/relicstrings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/eng/potionstrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/eng/uistrings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/eng/characterstrings.json");
        
        logger.info("Done adding strings");
    }

    @Override
    public void receiveEditKeywords() {
        logger.info("Adding keywords");

        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/keywordstrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
        logger.info("Done adding keywords!");
    }

    @Override
    public void receiveAddAudio() {
        logger.info("Adding audio");
        BaseMod.addAudio("HammerHit", getModID() + "Resources/sounds/hammer.ogg");
        BaseMod.addAudio("IN_FOR_THE_KILL", getModID() + "Resources/sounds/kill.ogg");
        logger.info("Done adding audio!");
    }
    
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    @Override
    public void receivePostPotionUse(AbstractPotion pot) {
        if (Wiz.adp().hasPower(BombLauncher.BombLauncherPower.POWER_ID)) {
            AbstractCreature target = null;
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                if (m.hb.hovered && !m.isDying)
                    target = m;
            if (target == null)
                target = AbstractDungeon.getRandomMonster();
            ((BombLauncher.BombLauncherPower) Wiz.adp().getPower(BombLauncher.BombLauncherPower.POWER_ID)).onPotionUse(pot, target);
            if (Wiz.adp().hasPower(Chemistry.ChemistryPower.POWER_ID))
                ((Chemistry.ChemistryPower)Wiz.adp().getPower(Chemistry.ChemistryPower.POWER_ID)).onPotionUse(pot);
        }
        if (Wiz.adp().hasPower(Chemistry.ChemistryPower.POWER_ID))
            ((Chemistry.ChemistryPower) Wiz.adp().getPower(Chemistry.ChemistryPower.POWER_ID)).onPotionUse(pot);
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (Wiz.adp().hasRelic(BlackBox.ID) && power instanceof ArtifactPower && target instanceof AbstractMonster)
            ((BlackBox) Wiz.adp().getRelic(BlackBox.ID)).onTrigger(power, target);
    }
}
