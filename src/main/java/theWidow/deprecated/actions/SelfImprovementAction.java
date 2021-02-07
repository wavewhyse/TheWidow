package theWidow.deprecated.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@Deprecated
public class SelfImprovementAction extends AbstractGameAction {

    private static final float DURATION = Settings.ACTION_DUR_FAST;

    private final AbstractPlayer p;

    public SelfImprovementAction(final AbstractPlayer p) {
        actionType = ActionType.CARD_MANIPULATION;
        this.p = p;
        duration = DURATION;
    }

    @Override
    public void update() {
        if (duration == DURATION) {
            if (p.hand.size() == 0) {
                isDone = true;
                return;
            }
            if (p.hand.size() == 1) {
                eatCard(p.hand.getTopCard());
                isDone = true;
                return;
            }
            if (p.hand.size() > 1) {
                AbstractDungeon.handCardSelectScreen.open("exhaust", 1, false, false, false, false);
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                eatCard(c);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            isDone = true;
        }
        tickDuration();
    }

    private void eatCard(AbstractCard c) {
        int upgrades = c.timesUpgraded;
        p.hand.moveToExhaustPile(c);
        if (upgrades > 0) {
            addToBot(new GainEnergyAction(upgrades));
            addToBot(new DrawCardAction(upgrades));
        }
    }
}
