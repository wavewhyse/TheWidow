package theWidow.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theWidow.relics.WaterFilterRelic;

public class WaterFilterPatch {

    private static final Logger logger = LogManager.getLogger(WaterFilterPatch.class.getName());

    @SpirePatch(
            clz = PotionPopUp.class,
            method = "updateTargetMode"
    )
    public static class WaterFilterPotionPopUpTargetModePatch {
        @SpireInsertPatch(
                locator = DestroyPotionLocator.class
        )
        public static SpireReturn chanceToNotUsePotion(PotionPopUp __instance) {
            if ( AbstractDungeon.player.hasRelic(WaterFilterRelic.ID) && ((WaterFilterRelic) AbstractDungeon.player.getRelic(WaterFilterRelic.ID)).potionSaveChance()) {
                AbstractDungeon.player.getRelic(WaterFilterRelic.ID).flash();
                __instance.targetMode = false;
                GameCursor.hidden = false;
                return SpireReturn.Return(null);
            }
            else
                return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = PotionPopUp.class,
            method = "updateInput"
    )
    public static class WaterFilterPotionPopUpInputPatch {
        @SpireInsertPatch(
                locator = DestroyPotionLocator.class
        )
        public static SpireReturn chanceToNotUsePotion(PotionPopUp __instance) {
            if (AbstractDungeon.player.hasRelic(WaterFilterRelic.ID) && ((WaterFilterRelic) AbstractDungeon.player.getRelic(WaterFilterRelic.ID)).potionSaveChance()) {
                AbstractDungeon.player.getRelic(WaterFilterRelic.ID).flash();
                __instance.close();
                return SpireReturn.Return(null);
            }
            else
                return SpireReturn.Continue();
        }
    }

    private static class DestroyPotionLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            return LineFinder.findInOrder(ctMethodToPatch, new Matcher.MethodCallMatcher(TopPanel.class, "destroyPotion"));
        }
    }
}