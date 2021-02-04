package theWidow.deprecated.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theWidow.potions.DistilledCardPotion;

public class DistillAction extends AbstractGameAction {

    private static final float DURATION = Settings.ACTION_DUR_FAST;

    private AbstractPlayer p;

    public DistillAction() {
        actionType = ActionType.CARD_MANIPULATION;
        duration = DURATION;
        p = AbstractDungeon.player;
    }

    @Override
    public void update() {
        if (duration == DURATION) {
            if (p.hand.size() == 0) {
                isDone = true;
                return;
            }
            else if (p.hand.size() == 1) {
                doDistill(p.hand.getTopCard());
                isDone = true;
                return;
            }
            else if (p.hand.size() > 1) {
                AbstractDungeon.handCardSelectScreen.open("distill", 1, false, false, false, false);
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group)
                doDistill(c);
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            isDone = true;
        }
        tickDuration();
    }

    private void doDistill(AbstractCard c) {
        addToBot(new ObtainPotionAction(new DistilledCardPotion(c.makeStatEquivalentCopy())));
        p.hand.moveToExhaustPile(c);
    }
}
