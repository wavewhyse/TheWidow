package theWidow.relics;

import basemod.abstracts.CustomRelic;
import theWidow.WidowMod;
import theWidow.powers.WebPower;
import theWidow.util.TexLoader;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class HourglassMark extends CustomRelic {
    public static final String ID = WidowMod.makeID(HourglassMark.class.getSimpleName());

    public static final int TRIGGER_AMOUNT = 6;

    public HourglassMark() {
        super( ID,
                TexLoader.getTexture(makeRelicPath(HourglassMark.class.getSimpleName())),
                TexLoader.getTexture(makeRelicOutlinePath(HourglassMark.class.getSimpleName())),
                RelicTier.UNCOMMON,
                LandingSound.MAGICAL );
    }

    @Override
    public void atBattleStart() {
        Wiz.apply(new WebPower(Wiz.adp(), TRIGGER_AMOUNT));
        flash();
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], TRIGGER_AMOUNT);
    }

}
