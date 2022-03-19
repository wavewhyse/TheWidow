package theWidow.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import theWidow.WidowMod;
import theWidow.util.Wiz;

import java.util.ArrayList;
import java.util.List;

public class WidowUpgradeManagerAction extends AbstractGameAction {
    private static final UIStrings uistrings = CardCrawlGame.languagePack.getUIString(WidowMod.makeID(WidowUpgradeManagerAction.class.getSimpleName()));

    private final List<AbstractCard> cannotUpgrade = new ArrayList<>();
    private final boolean random;
    private final boolean permanent;

    private static final float DURATION = Settings.ACTION_DUR_XFAST;

    public WidowUpgradeManagerAction() {
        this(1, false, false);
    }

    public WidowUpgradeManagerAction(final int amount) {
        this(amount, false, false);
    }

    public WidowUpgradeManagerAction(final boolean random) {
        this(1, random, false);
    }

    public WidowUpgradeManagerAction(final int amount, final boolean random) {
        this(amount, random, false);
    }

    public WidowUpgradeManagerAction(final int amount, final boolean random, final boolean permanent) {
        this.amount = amount;
        this.permanent = permanent;
        this.random = random;
        actionType = ActionType.CARD_MANIPULATION;
        duration = DURATION;
    }
    
    @Override
    public void update() {
        if (duration == DURATION) {
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                isDone = true;
                return;
            }
            for (AbstractCard c : Wiz.adp().hand.group) {
                if (!c.canUpgrade())
                    cannotUpgrade.add(c);
            }
            if (cannotUpgrade.size() == Wiz.adp().hand.size()) {
                isDone = true;
                return;
            }
            if (Wiz.adp().hand.size() - cannotUpgrade.size() <= amount) {
                for (AbstractCard c : Wiz.adp().hand.group) {
                    if (c.canUpgrade())
                        addToTop(new WidowUpgradeCardAction(c, permanent));
                }
                isDone = true;
                return;
            }
            if (random) {
                CardGroup cardsToUpgrade = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard c : Wiz.adp().hand.group)
                    if (c.canUpgrade())
                        cardsToUpgrade.addToTop(c);
                for (int i=0; i<amount && !cardsToUpgrade.isEmpty(); i++) {
                    AbstractCard c = cardsToUpgrade.getRandomCard(AbstractDungeon.cardRandomRng);
                    if (c.canUpgrade())
                        addToTop(new WidowUpgradeCardAction(c, permanent));
                    cardsToUpgrade.removeCard(c);
                }
                isDone = true;
                return;
            }

            Wiz.adp().hand.group.removeAll(cannotUpgrade);
            if (Wiz.adp().hand.size() <= amount) {
                for (AbstractCard c : Wiz.adp().hand.group)
                    addToTop(new WidowUpgradeCardAction(c, permanent));
                returnCards();
                isDone = true;
            }
            else {
                AbstractDungeon.handCardSelectScreen.open(uistrings.TEXT[0], amount, false, false, false, true);
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                addToTop(new WidowUpgradeCardAction(c, permanent));
                Wiz.adp().hand.addToTop(c);
            }
            returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            isDone = true;
        }
        tickDuration();
    }

    private void returnCards() {
        for (AbstractCard c : cannotUpgrade)
            Wiz.adp().hand.addToTop(c);
        Wiz.adp().hand.refreshHandLayout();
    }
}
