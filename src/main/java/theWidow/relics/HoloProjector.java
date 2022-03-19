package theWidow.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theWidow.WidowMod;
import theWidow.actions.WidowDowngradeCardAction;
import theWidow.util.TexLoader;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class HoloProjector extends CustomRelic {
    public static final String ID = WidowMod.makeID(HoloProjector.class.getSimpleName());

    public static final int DOWNGRADES = 2;

    public HoloProjector() {
        super( ID,
                TexLoader.getTexture(makeRelicPath(HoloProjector.class.getSimpleName())),
                TexLoader.getTexture(makeRelicOutlinePath(HoloProjector.class.getSimpleName())),
                RelicTier.BOSS,
                LandingSound.CLINK );
    }

    @Override
    public void atTurnStartPostDraw() {
        flash();
        addToBot(new RelicAboveCreatureAction(Wiz.adp(), this));
        addToBot(new HoloProjectorAction());
    }

    @Override
    public void onEquip() {
        Wiz.adp().energy.energyMaster++;
    }

    @Override
    public void onUnequip() {
        Wiz.adp().energy.energyMaster--;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], DOWNGRADES);
    }

    private static class HoloProjectorAction extends AbstractGameAction {
        @Override
        public void update() {
            CardGroup upgraded = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : Wiz.adp().hand.group)
                if (c.upgraded)
                    upgraded.addToTop(c);
            for (int i=0; i<DOWNGRADES && !upgraded.isEmpty(); i++) {
                AbstractCard c = upgraded.getRandomCard(AbstractDungeon.cardRandomRng);
                addToTop(new WidowDowngradeCardAction(c));
                upgraded.removeCard(c);
            }
            isDone = true;
        }
    }
}
