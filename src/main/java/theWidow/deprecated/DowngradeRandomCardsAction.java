package theWidow.deprecated;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theWidow.WidowMod;

@Deprecated
public class DowngradeRandomCardsAction extends AbstractGameAction {

    private static final float DURATION = Settings.ACTION_DUR_FASTER;
    private final int amount;

    public DowngradeRandomCardsAction(final int amount) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = DURATION;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (duration == DURATION) {
            if (AbstractDungeon.player.hand.size() <= 0) {
                isDone = true;
                return;
            }
            CardGroup upgraded = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c.upgraded)
                    upgraded.addToTop(c);
            }
            if (upgraded.size() <= amount) {
                for (AbstractCard c : upgraded.group) {
                    addToBot(new DowngradeCardAction(c));
                    c.superFlash();
                }
            } else {
                upgraded.shuffle(AbstractDungeon.cardRng);
                for (AbstractCard c : upgraded.group)
                WidowMod.downgradeCard(c);
            }
        }
        tickDuration();
    }
}
