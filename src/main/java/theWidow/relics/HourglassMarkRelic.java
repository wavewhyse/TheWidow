package theWidow.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theWidow.WidowMod;
import theWidow.powers.WebPower2;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class HourglassMarkRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = WidowMod.makeID("HourglassMarkRelic");

    public static final int TRIGGER_AMOUNT = 5;
    public static final int STAT_GAIN = 1;

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("HourglassMark.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("HourglassMark.png"));

    public HourglassMarkRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
        pulse = false;
    }

//    @Override
//    public void onTrigger() {
//        AbstractPlayer p = AbstractDungeon.player;
//        int webNumber = 0;
//        if (p.hasPower(WebPower2.POWER_ID))
//            webNumber = p.getPower(WebPower2.POWER_ID).amount;
//        if (!pulse) {
//            if (webNumber >= TRIGGER_AMOUNT){
//                flash();
//                addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, STAT_GAIN), STAT_GAIN));
//                addToTop(new ApplyPowerAction(p, p, new DexterityPower(p, STAT_GAIN), STAT_GAIN));
//                beginPulse();
//                pulse = true;
//            }
//        } else {
//            if (webNumber < TRIGGER_AMOUNT){
//                addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, -STAT_GAIN), -STAT_GAIN));
//                addToTop(new ApplyPowerAction(p, p, new DexterityPower(p, -STAT_GAIN), -STAT_GAIN));
//                pulse = false;
//                stopPulse();
//            }
//
//        }
//    }
//
//    @Override
//    public void onVictory() {
//        pulse = false;
//    }

    @Override
    public void atBattleStart() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new WebPower2(AbstractDungeon.player, TRIGGER_AMOUNT)));
        flash();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TRIGGER_AMOUNT + DESCRIPTIONS[1];// + STAT_GAIN + DESCRIPTIONS[2] + STAT_GAIN + DESCRIPTIONS[3];
    }

}
