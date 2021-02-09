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

public class CyberheartRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = WidowMod.makeID("CyberheartRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Cyberheart.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Cyberheart.png"));

    private static final int UPGRADES = 3;

    public CyberheartRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
    }


    // Gain 1 Strength on on equip.
    @Override
    public void atBattleStart() {
        flash();
        addToBot(new RelicAboveCreatureAction((AbstractCreature)AbstractDungeon.player, this));
        addToBot(new WidowUpgradeManagerAction(UPGRADES, true));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + UPGRADES + DESCRIPTIONS[1];
    }

}
