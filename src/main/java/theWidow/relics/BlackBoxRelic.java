package theWidow.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class BlackBoxRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = WidowMod.makeID("BlackBoxRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BlackBox.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("BlackBox.png"));

    public BlackBoxRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    public void onTrigger(AbstractPower p, AbstractCreature c) {
        addToTop(new RemoveSpecificPowerAction(c, c, p));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
