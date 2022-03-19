package theWidow.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeManagerAction;
import theWidow.util.TexLoader;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class Cyberheart extends CustomRelic {
    public static final String ID = WidowMod.makeID(Cyberheart.class.getSimpleName());

    public static final int UPGRADES = 3;

    public Cyberheart() {
        super( ID,
                TexLoader.getTexture(makeRelicPath(Cyberheart.class.getSimpleName())),
                TexLoader.getTexture(makeRelicOutlinePath(Cyberheart.class.getSimpleName())),
                RelicTier.STARTER,
                LandingSound.CLINK );
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(Wiz.adp(), this));
        addToBot(new WidowUpgradeManagerAction(UPGRADES, true));
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], UPGRADES);
    }

}
