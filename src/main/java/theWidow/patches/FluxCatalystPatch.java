package theWidow.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.campfire.SmithOption;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSmithEffect;
import javassist.CtBehavior;
import theWidow.relics.FluxCatalyst;
import theWidow.util.Wiz;

@SpirePatch(
        clz = SmithOption.class,
        method = "useOption"
)
public final class FluxCatalystPatch {

    @SpireInsertPatch (
            locator = SmithSwitcheroo.class
    )
    public static SpireReturn fluxCatalyst() {
        if (Wiz.adp().hasRelic(FluxCatalyst.ID)) {
            AbstractDungeon.effectList.add(new FluxCatalyst.FluxCatalystSmithEffect());
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
