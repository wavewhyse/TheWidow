package theWidow.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import theWidow.WidowMod;
import theWidow.cards.BetaCard;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class SewingKitRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = WidowMod.makeID("SewingKitRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("SewingKit.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("SewingKit.png"));

    public SewingKitRelic() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public void onEquip() {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c instanceof BetaCard)
                c.upgrade();
        }
        for (RewardItem reward : AbstractDungeon.combatRewardScreen.rewards) {
            if (reward.cards != null)
                for (AbstractCard c : reward.cards)
                    if (c instanceof BetaCard)
                        c.upgrade();
        }
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
