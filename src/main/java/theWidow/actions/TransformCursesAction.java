package theWidow.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.TransformCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theWidow.util.Wiz;

public class TransformCursesAction extends AbstractGameAction {

    public TransformCursesAction() {
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        for (int i = 0; i < Wiz.adp().hand.size(); i++)
            if (Wiz.adp().hand.group.get(i).type == AbstractCard.CardType.STATUS || Wiz.adp().hand.group.get(i).type == AbstractCard.CardType.CURSE)
                addToTop(new TransformCardInHandAction(i, AbstractDungeon.returnTrulyRandomCardInCombat()));
        isDone = true;
    }
}
