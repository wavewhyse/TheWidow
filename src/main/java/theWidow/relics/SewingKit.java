package theWidow.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import theWidow.WidowMod;
import theWidow.cards.BetaCard;
import theWidow.util.TexLoader;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class SewingKit extends CustomRelic {
    public static final String ID = WidowMod.makeID(SewingKit.class.getSimpleName());

    public SewingKit() {
        super( ID,
                TexLoader.getTexture(makeRelicPath(SewingKit.class.getSimpleName())),
                TexLoader.getTexture(makeRelicOutlinePath(SewingKit.class.getSimpleName())),
                RelicTier.COMMON,
                LandingSound.FLAT );
    }

    @Override
    public void onEquip() {
        for (AbstractCard c : Wiz.adp().masterDeck.group) {
            if (c instanceof BetaCard)
                c.upgrade();
        }
        for (RewardItem reward : AbstractDungeon.combatRewardScreen.rewards) {
            if (reward.cards != null)
                for (AbstractCard c : reward.cards)
                    if (c instanceof BetaCard)
                        c.upgrade();
        }
        if (AbstractDungeon.shopScreen != null) {
            for (AbstractCard c : AbstractDungeon.shopScreen.coloredCards)
                if (c instanceof BetaCard)
                    c.upgrade();
            for (AbstractCard c : AbstractDungeon.shopScreen.colorlessCards)
                if (c instanceof BetaCard)
                    c.upgrade();
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
