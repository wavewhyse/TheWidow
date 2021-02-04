package theWidow.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnUsePotionRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class WaterFilterRelic extends CustomRelic implements BetterOnUsePotionRelic {

    // ID, images, text.
    public static final String ID = WidowMod.makeID("WaterFilterRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("WaterFilter.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("WaterFilter.png"));

    public WaterFilterRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    private static final int PERCENT_CHANCE = 25;

    @Override
    public void betterOnUsePotion(AbstractPotion abstractPotion) {
        /*if (potionSaveChance()) {
            flash();
            addToTop(new ObtainPotionAction(abstractPotion.makeCopy()));
        }*/
    }

    public boolean potionSaveChance() {
        return AbstractDungeon.cardRandomRng.randomBoolean() && AbstractDungeon.cardRandomRng.randomBoolean();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + PERCENT_CHANCE + DESCRIPTIONS[1];
    }

}
