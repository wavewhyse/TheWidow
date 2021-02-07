package theWidow.deprecated.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

@Deprecated
public class MeltdownAction extends AbstractGameAction {
    private AbstractPlayer p;

    private ArrayList<AbstractCard> unupgraded = new ArrayList<>();
    private int copiesToCreate;

    private static final float DURATION = Settings.ACTION_DUR_FAST;

    public MeltdownAction(final AbstractPlayer p, final int amount) {
        this.p = p;
        copiesToCreate = amount;
        actionType = ActionType.CARD_MANIPULATION;
        duration = DURATION;
    }
    
    @Override
    public void update() {
        if (duration == DURATION) {
            for (AbstractCard c : p.hand.group) {
                if (!c.upgraded || c.tags.contains(AbstractCard.CardTags.HEALING))
                    unupgraded.add(c);
            }
            if (unupgraded.size() == p.hand.size()) {
                isDone = true;
                return;
            }

            if (p.hand.size() - unupgraded.size() == 1) {
                for (AbstractCard c : p.hand.group) {
                    if (c.upgraded) {
                        meltDown(c);
                        isDone = true;
                        return;
                    }
                }
            }
            p.hand.group.removeAll(unupgraded);
            if (p.hand.size() > 1) {
                AbstractDungeon.handCardSelectScreen.open("melt down", 1, false, false, false, false);
                tickDuration();
                return;
            }
            if (p.hand.size() == 1) {
                meltDown(p.hand.getTopCard());
                returnCards();
                isDone = true;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                meltDown(c);
            }
            returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            isDone = true;
        }
        tickDuration();
    }

    private void meltDown(AbstractCard c) {
        for (int i = 0; i < copiesToCreate; i++)
            addToTop(new MakeTempCardInHandAction(c.makeCopy()));
        p.hand.moveToExhaustPile(c);
    }

    private void returnCards() {
        for (AbstractCard c : this.unupgraded)
            this.p.hand.addToTop(c);
        this.p.hand.refreshHandLayout();
    }
}
