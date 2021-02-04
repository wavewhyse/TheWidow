package theWidow.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theWidow.powers.WebPower;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "calculateDamage"
)
public class WebIntentPatch {

    private static final Logger logger = LogManager.getLogger(ParalysisPatch.class.getName());

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"tmp", "p"}
    )
    public static void dontCheckWeb(AbstractMonster __instance, int dmg, @ByRef float[] tmp, AbstractPower p) {
        if (p instanceof WebPower)
            tmp[0] *= 2f;
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPower.class, "atDamageFinalReceive");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}