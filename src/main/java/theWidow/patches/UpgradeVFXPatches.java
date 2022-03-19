package theWidow.patches;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.UpgradeRandomCardAction;
import com.megacrit.cardcrawl.actions.unique.ApotheosisAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeManagerAction;
import theWidow.util.Wiz;

public final class UpgradeVFXPatches {
    @SpirePatch(
            clz = ArmamentsAction.class,
            method = "update"
    )
    public static final class ArmamentsUpgradeVFX {
        public static SpireReturn Prefix(ArmamentsAction __instance, boolean ___upgraded) {
            if (WidowMod.enableBOTFPatch) {
                if (___upgraded)
                    Wiz.adam().addToTop(new WidowUpgradeManagerAction(BaseMod.MAX_HAND_SIZE));
                else
                    Wiz.adam().addToTop(new WidowUpgradeManagerAction());
                __instance.isDone = true;
                return SpireReturn.Return();
            }
            else
                return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = UpgradeRandomCardAction.class,
            method = "update"
    )
    public static final class RandomUpgradeVFX {
        public static SpireReturn Prefix(UpgradeRandomCardAction __instance) {
            if (WidowMod.enableBOTFPatch) {
                Wiz.adam().addToTop(new WidowUpgradeManagerAction(true));
                __instance.isDone = true;
                return SpireReturn.Return();
            }
            else
                return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = ApotheosisAction.class,
            method = "upgradeAllCardsInGroup"
    )
    public static final class ApotheosisUpgradeVFX {
        public static SpireReturn Prefix(ApotheosisAction __instance, CardGroup ___cardGroup) {
            if (WidowMod.enableBOTFPatch && ___cardGroup == Wiz.adp().hand) {
                Wiz.adam().addToTop(new WidowUpgradeManagerAction(BaseMod.MAX_HAND_SIZE));
                return SpireReturn.Return();
            }
            else
                return SpireReturn.Continue();
        }
    }
}
