package theWidow.patches;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.BlessingOfTheForge;
import com.megacrit.cardcrawl.relics.SacredBark;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeManagerAction;

import java.util.ArrayList;

public class BlessingOfTheForgePatch {

    private static final Logger logger = LogManager.getLogger(BlessingOfTheForgePatch.class.getName());

    @SpirePatch(
            clz = BlessingOfTheForge.class,
            method = "use"
    )
    public static class UsePatch {
        @SpireInsertPatch(
                locator = UseLocator.class
        )
        public static SpireReturn changeAction(BlessingOfTheForge __instance) {
            if (WidowMod.enableBOTFPatch) {
                AbstractDungeon.actionManager.addToBottom(new WidowUpgradeManagerAction(BaseMod.MAX_HAND_SIZE));
                if (AbstractDungeon.player.hasRelic(SacredBark.ID))
                    AbstractDungeon.actionManager.addToBottom(new WidowUpgradeManagerAction(BaseMod.MAX_HAND_SIZE));
                return SpireReturn.Return();
            } else
                return SpireReturn.Continue();
        }
    }

    private static class UseLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            return LineFinder.findInOrder(ctMethodToPatch, new Matcher.NewExprMatcher(ArmamentsAction.class));
        }
    }

    @SpirePatch(
            clz = BlessingOfTheForge.class,
            method = "initializeData"
    )
    public static class InitializeDataPatch {
        @SpireInsertPatch(
                locator = InitializeDataLocator.class
        )
        public static void doubleDescription(BlessingOfTheForge __instance) {
            if (WidowMod.enableBOTFPatch && AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SacredBark.ID))
                __instance.description = CardCrawlGame.languagePack.getPotionString("BlessingOfTheForgeWithSacredBark").DESCRIPTIONS[0];
        }
    }
    private static class InitializeDataLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            return LineFinder.findInOrder(ctMethodToPatch, new Matcher.MethodCallMatcher(ArrayList.class, "clear"));
        }
    }
}