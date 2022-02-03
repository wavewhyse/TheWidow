//package theWidow.patches;
//
//import com.evacipated.cardcrawl.modthespire.lib.*;
//import com.megacrit.cardcrawl.cards.DamageInfo;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import javassist.CtBehavior;
//import theWidow.powers.WebPower;
//
//@SpirePatch(
//        clz = AbstractMonster.class,
//        method = "calculateDamage"
//)
//public class WebIntentPatch {
//
//    @SpireInsertPatch(
//            locator = Locator.class,
//            localvars = {"tmp", "p"}
//    )
//    public static void betterWebDamageReceive(AbstractMonster __instance, int dmg, @ByRef float[] tmp, AbstractPower p) {
//        if (p instanceof WebPower) {
//            tmp[0] = ((WebPower) p).atDamageReceiveButPassTheActualDamageSource(tmp[0], DamageInfo.DamageType.NORMAL, __instance);
//            tmp[0] /= WebPower.DAMAGE_MULT;
//        }
//    } //Incredible.
//
//    private static class Locator extends SpireInsertLocator {
//        @Override
//        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
//            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPower.class, "atDamageFinalReceive");
//            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
//        }
//    }
//}
