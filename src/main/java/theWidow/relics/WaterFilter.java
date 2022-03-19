package theWidow.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theWidow.WidowMod;
import theWidow.util.TexLoader;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class WaterFilter extends CustomRelic {
    public static final String ID = WidowMod.makeID(WaterFilter.class.getSimpleName());

    private static final int PERCENT_CHANCE = 25;

    public WaterFilter() {
        super( ID,
                TexLoader.getTexture(makeRelicPath(WaterFilter.class.getSimpleName())),
                TexLoader.getTexture(makeRelicOutlinePath(WaterFilter.class.getSimpleName())),
                RelicTier.RARE,
                LandingSound.SOLID );
    }

    public boolean potionSaveChance() {
        return AbstractDungeon.cardRandomRng.random(100) <= PERCENT_CHANCE;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], PERCENT_CHANCE);
    }

}
