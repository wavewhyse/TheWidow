package theWidow.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

public class TransformChoiceCardAction extends AbstractGameAction {

    private static final float DURATION = Settings.ACTION_DUR_MED;

    public TransformChoiceCardAction() {
        actionType = ActionType.CARD_MANIPULATION;
        duration = DURATION;
    }

    @Override
    public void update() {
        if (duration == DURATION) {

        }
        tickDuration();
    }
}
