package theWidow.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theWidow.util.Wiz;

import java.util.function.BiFunction;

public class DoToNewCardAction extends AbstractGameAction {
    public BiFunction<AbstractCard, Object[], Boolean> cardBiFunction;
    public Object[] params;
    private AbstractCard card;

    public DoToNewCardAction(BiFunction<AbstractCard, Object[], Boolean> cardBiFunction, Object... params) {
        this.cardBiFunction = cardBiFunction;
        this.params = params;
    }

    @Override
    public void update() {
        if (Wiz.adp().hand.isEmpty()) {
            isDone = true;
            return;
        }
        if (card == null)
            card = Wiz.adp().hand.getTopCard();
        isDone = cardBiFunction.apply(card, params);
    }
}
