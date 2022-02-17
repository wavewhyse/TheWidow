package theWidow.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.campfire.SmithOption;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSmithEffect;
import javassist.CtBehavior;
import theWidow.relics.FluxCatalystRelic;

@SpirePatch(
        clz = SmithOption.class,
        method = "useOption"
)
public class FluxCatalystPatch {

    @SpireInsertPatch (
            locator = SmithSwitcheroo.class
    )
    public static SpireReturn fluxCatalyst() {
        if (AbstractDungeon.player.hasRelic(FluxCatalystRelic.ID)) {
            AbstractDungeon.effectList.add(new FluxCatalystRelic.FluxCatalystSmithEffect());
            return SpireReturn.Return();
        }
        else return SpireReturn.Continue();
    }

    public static class SmithSwitcheroo extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            return LineFinder.findInOrder(ctMethodToPatch, new Matcher.NewExprMatcher(CampfireSmithEffect.class));
        }
    }
}
