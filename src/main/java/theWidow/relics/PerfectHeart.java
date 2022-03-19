package theWidow.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeManagerAction;
import theWidow.util.TexLoader;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class PerfectHeart extends CustomRelic {
    public static final String ID = WidowMod.makeID(PerfectHeart.class.getSimpleName());

    private static final int UPGRADES = 2;

    public PerfectHeart() {
        super( ID,
                TexLoader.getTexture(makeRelicPath(PerfectHeart.class.getSimpleName())),
                TexLoader.getTexture(makeRelicOutlinePath(Cyberheart.class.getSimpleName())),
                RelicTier.BOSS,
                LandingSound.CLINK );
    }

    @Override
    public void obtain() {
        if (Wiz.adp().hasRelic(Cyberheart.ID)) {
            for (int i = 0; i < Wiz.adp().relics.size(); ++i) {
                if (Wiz.adp().relics.get(i).relicId.equals(Cyberheart.ID)) {
                    instantObtain(Wiz.adp(), i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    @Override
    public void atTurnStartPostDraw() {
        flash();
        addToBot(new RelicAboveCreatureAction(Wiz.adp(), this));
        addToBot(new WidowUpgradeManagerAction(UPGRADES, true));
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], UPGRADES);
    }

    @Override
    public boolean canSpawn() {
        return Wiz.adp().hasRelic(Cyberheart.ID);
    }
}
