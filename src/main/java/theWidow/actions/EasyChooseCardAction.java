package theWidow.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theWidow.util.Wiz;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EasyChooseCardAction extends AbstractGameAction {
    public Predicate<AbstractCard> condition;
    public String selectMessage;
    public BiFunction<AbstractCard, Object[], Boolean> choiceActionUpdate;
    public Object[] params;
    private List<AbstractCard> nonMatching;
    private List<AbstractCard> chosenCards;

    private final ArrayList<AbstractCard> hand = Wiz.adp().hand.group;

    /**
     * @param amount             The number of cards to be chosen.
     * @param condition          A Predicate to filter which cards in hand should be eligible for selection.
     * @param selectMessage      The text that will be shown on the selection screen.
     * @param choiceActionUpdate A BiFunction that receives an AbstractCard and any number of parameters in the form of an array. This function will be called on each card that is chosen until that function returns true. The action is done once the function has returned true for all selected cards.
     * @param params        Any number of parameters. These will be passed to the update function to avoid possible value changes between the creation of this action and when it is updated.
     */
    public EasyChooseCardAction(int amount, Predicate<AbstractCard> condition, String selectMessage, BiFunction<AbstractCard, Object[], Boolean> choiceActionUpdate, Object... params) {
        this.amount = amount;
        this.condition = condition;
        this.selectMessage = selectMessage;
        this.choiceActionUpdate = choiceActionUpdate;
        this.params = params;
        duration = startDuration = 0.1f;
        nonMatching = new ArrayList<>();
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            nonMatching = hand.stream().filter(condition.negate()).collect(Collectors.toList());

            if (hand.size() <= nonMatching.size()) {
                isDone = true;
                return;
            }

            if (hand.size() <= nonMatching.size() + amount)
                chosenCards = hand.stream().filter(condition).collect(Collectors.toList());
            else {
                hand.removeAll(nonMatching);
                AbstractDungeon.handCardSelectScreen.open(selectMessage, amount, true);
            }
            tickDuration();
            return;
        }
        if (chosenCards == null && !AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            chosenCards = new ArrayList<>(AbstractDungeon.handCardSelectScreen.selectedCards.group);
            hand.addAll(chosenCards);
            hand.addAll(nonMatching);
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
        }
        if (chosenCards != null) {
            chosenCards.removeIf(card -> choiceActionUpdate.apply(card, params));
            if (chosenCards.isEmpty()) {
                isDone = true;
            }
        }
    }
}