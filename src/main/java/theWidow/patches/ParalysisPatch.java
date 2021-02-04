package theWidow.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theWidow.powers.ParalysisPower;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "createIntent"
)
public class ParalysisPatch {

    private static final Logger logger = LogManager.getLogger(ParalysisPatch.class.getName());

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"intentMultiAmt", "isMultiDmg"}
    )
    public static void adjustIntentForParalysisAndPrimeParalysisPower(
            AbstractMonster __instance, @ByRef int[] intentMultiAmt, @ByRef boolean[] isMultiDmg) {
        if (__instance.hasPower(ParalysisPower.POWER_ID) && intentMultiAmt[0] > 1) {    //if paralyzed and multiattacking, attack 1 less time.
            intentMultiAmt[0]--;
            if (intentMultiAmt[0] < 2) {
                intentMultiAmt[0] = -1;
                isMultiDmg[0] = false;
            }
            ((ParalysisPower) __instance.getPower(ParalysisPower.POWER_ID)).primed = true;
        }
        // Incredible.
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "getIntentBg");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    @SpirePatch(
            clz = DamageAction.class,
            method = "update")
    public static class ParalysisDamagePatch {
        @SpirePrefixPatch
        public static SpireReturn preventFirstDamageWhileParalyzedIfPrimed(DamageAction __instance, DamageInfo ___info) {
            if (    //if the damage source is a monster with unactivated paralysis dealing attack damage
                    ___info.owner instanceof AbstractMonster &&
                            ___info.type == DamageInfo.DamageType.NORMAL &&
                            ___info.owner.hasPower(ParalysisPower.POWER_ID) &&
                            ((ParalysisPower) ___info.owner.getPower(ParalysisPower.POWER_ID)).primed
            ) {
                ((ParalysisPower) ___info.owner.getPower(ParalysisPower.POWER_ID)).primed = false;
                __instance.isDone = true;
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
        // Incredible.
    }
}
