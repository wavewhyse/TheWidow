package theWidow.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class SalvageAction extends AbstractGameAction {

    private static final float DURATION = Settings.ACTION_DUR_MED;

    public SalvageAction() {
        actionType = ActionType.CARD_MANIPULATION;
        duration = DURATION;
    }
    
    @Override
    public void update() {
        if (duration == DURATION) {
            ArrayList<AbstractCard> cardsToDiscard = new ArrayList<AbstractCard>();
            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if (!c.upgraded) {
                    cardsToDiscard.add(c);
                }
            }
            for (AbstractCard c : cardsToDiscard) {
                AbstractDungeon.player.drawPile.moveToDiscardPile(c);
            }
        }
        tickDuration();
    }
}
