package theWidow.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theWidow.cards.uncommon.Disruptor;

public class DisruptorPatch {

    private static final Logger logger = LogManager.getLogger(DisruptorPatch.class.getName());

    @SpirePatch(
            clz = GainBlockAction.class,
            method = "update"
    )
    public static final class WaterFilterPotionPopUpTargetModePatch {
        @SpirePrefixPatch
        public static SpireReturn preventBlock(GainBlockAction __instance) {
            if (__instance.target.hasPower(Disruptor.DisruptorPower.POWER_ID)) {
                __instance.isDone = true;
                return SpireReturn.Return(null);
            }
            else
                return SpireReturn.Continue();
        }
    }
}