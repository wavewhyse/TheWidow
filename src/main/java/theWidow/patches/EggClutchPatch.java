package theWidow.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class EggClutchPatch {

    private static final Logger logger = LogManager.getLogger(EggClutchPatch.class.getName());
    public static int EGG_CLUTCH_UPGRADES = 0;

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "getRewardCards"
    )
    public static final class upgradeCardRewards {

        @SpirePostfixPatch
        public static ArrayList<AbstractCard> eggClutchUpgrade(ArrayList<AbstractCard> __result) {
            if (EGG_CLUTCH_UPGRADES != 0) {
                List<AbstractCard> upgradeable = new ArrayList<>(__result);
                upgradeable.removeIf(abstractCard -> !abstractCard.canUpgrade());
                ArrayList<Boolean> upgrades = new ArrayList<>();
                for (int i = 0; i < EGG_CLUTCH_UPGRADES && i < upgradeable.size(); i++)
                    upgrades.add(true);
                EGG_CLUTCH_UPGRADES = 0;
                for (int i = upgrades.size(); i < upgradeable.size(); i++)
                    upgrades.add(false);
                Collections.shuffle(upgrades, AbstractDungeon.cardRng.random);

                for (int i = 0; i < upgradeable.size(); i++)
                    if (upgrades.get(i))
                        upgradeable.get(i).upgrade();
            }
            return __result;
        }
    }
}