package theWidow.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theWidow.WidowMod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class EggClutchPatch {

    private static final Logger logger = LogManager.getLogger(EggClutchPatch.class.getName());

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "getRewardCards"
    )
    public static class upgradeCardRewards {

        @SpirePostfixPatch
        public static ArrayList<AbstractCard> eggClutchUpgrade(ArrayList<AbstractCard> __result) {
            ArrayList<AbstractCard> upgradeable = new ArrayList<>(__result);
            Iterator<AbstractCard> itr = upgradeable.iterator();
            while (itr.hasNext())
                if (!itr.next().canUpgrade())
                    itr.remove();
            ArrayList<Boolean> upgrades = new ArrayList<>();
            for (int i=0; i< WidowMod.EGG_CLUTCH_UPGRADES && i<upgradeable.size(); i++)
                upgrades.add(true);
            WidowMod.EGG_CLUTCH_UPGRADES = 0;
            for (int i=upgrades.size(); i<upgradeable.size(); i++)
                upgrades.add(false);
            Collections.shuffle(upgrades, AbstractDungeon.cardRng.random);

            for (int i=0; i<upgradeable.size(); i++)
                if (upgrades.get(i))
                    upgradeable.get(i).upgrade();

            return __result;
        }
    }
}