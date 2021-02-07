package theWidow.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeManagerAction;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class PerfectHeartRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = WidowMod.makeID("PerfectHeartRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Cyberheart2.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Cyberheart.png"));

    private static final int UPGRADES = 2;

    public PerfectHeartRelic() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(CyberheartRelic.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(CyberheartRelic.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
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
        addToBot(new RelicAboveCreatureAction((AbstractCreature)AbstractDungeon.player, this));
        addToBot(new WidowUpgradeManagerAction(AbstractDungeon.player, true, UPGRADES, false));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + UPGRADES + DESCRIPTIONS[1];
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(CyberheartRelic.ID);
    }
}
