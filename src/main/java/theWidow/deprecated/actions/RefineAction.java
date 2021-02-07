package theWidow.deprecated.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

@Deprecated
public class RefineAction extends AbstractGameAction {
    private AbstractPlayer player;
    private ArrayList<AbstractCard> cannotUpgrade = new ArrayList<>();

    private static final float DURATION = Settings.ACTION_DUR_FAST;

    public RefineAction() {
        actionType = ActionType.CARD_MANIPULATION;
        duration = DURATION;
        player = AbstractDungeon.player;
    }

    @Override
    public void update() {
        if (duration == DURATION) {
            if (player.discardPile.isEmpty() || player.hand.size() == 10) {
                isDone = true;
                return;
            }
            if (player.discardPile.size() == 1) {
                AbstractCard c = player.discardPile.getTopCard();
                if (c.canUpgrade()) {
                    refineCard(c, false);
                }
                isDone = true;
                return;
            }
            for (AbstractCard c : player.discardPile.group) {
                if (!c.canUpgrade())
                    cannotUpgrade.add(c);
            }
            if (cannotUpgrade.size() == player.discardPile.size()) {
                isDone = true;
                return;
            }
            if (cannotUpgrade.size() == player.discardPile.size() - 1) {
                AbstractCard toRefine = player.discardPile.getTopCard();
                for (AbstractCard c : player.discardPile.group) {
                    if (c.canUpgrade())
                        toRefine = c;
                }
                refineCard(toRefine, false);
                isDone = true;
                for (AbstractCard c : player.hand.group)
                    c.applyPowers();
                return;
            }
            player.discardPile.group.removeAll(cannotUpgrade);
            if (player.discardPile.size() > 1) {
                AbstractDungeon.gridSelectScreen.open(player.discardPile, 1, "refine", true, false, false, false);
                tickDuration();
                return;
            }
            if (player.discardPile.size() == 1) {
                refineCard(player.discardPile.getTopCard(), false);
                returnCards();
                isDone = true;
            }
            if (player.discardPile.size() == 0) {
                returnCards();
                isDone = true;
                return;
            }
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
                refineCard(c, true);
            for (AbstractCard c : player.discardPile.group) {
                c.unhover();
                c.target_x = CardGroup.DISCARD_PILE_X;
                c.target_y = 0.0F;
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            returnCards();
        }
        tickDuration();
        if (isDone)
            for (AbstractCard c : player.hand.group)
                c.applyPowers();
    }

    private void refineCard(AbstractCard c, boolean unhover) {
        player.hand.addToHand(c);
        player.discardPile.removeCard(c);
        c.upgrade();
        c.lighten(false);
        if (unhover)
            c.unhover();
        c.superFlash();
        c.applyPowers();
    }

    private void returnCards() {
        for (AbstractCard c : cannotUpgrade)
            player.discardPile.addToBottom(c);
        cannotUpgrade.clear();
    }
}
