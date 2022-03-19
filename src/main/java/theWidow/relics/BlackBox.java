package theWidow.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.util.TexLoader;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class BlackBox extends CustomRelic {
    public static final String ID = WidowMod.makeID(BlackBox.class.getSimpleName());

    public BlackBox() {
        super( ID,
                TexLoader.getTexture(makeRelicPath(BlackBox.class.getSimpleName())),
                TexLoader.getTexture(makeRelicOutlinePath(BlackBox.class.getSimpleName())),
                RelicTier.UNCOMMON,
                LandingSound.HEAVY );
    }

    public void onTrigger(AbstractPower p, AbstractCreature c) {
        addToTop(new RemoveSpecificPowerAction(c, c, p));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
