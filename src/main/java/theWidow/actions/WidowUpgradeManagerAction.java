package theWidow.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class WidowUpgradeManagerAction extends AbstractGameAction {
    private AbstractPlayer p;

    private ArrayList<AbstractCard> cannotUpgrade = new ArrayList<>();
    private final boolean random;
    private final boolean permanent;

    private static final float DURATION = Settings.ACTION_DUR_XFAST;

    public WidowUpgradeManagerAction(final AbstractPlayer p) {
        this(p, false, 1, false);
    }

    public WidowUpgradeManagerAction(final AbstractPlayer p, final int amount) {
        this(p, false, amount, false);
    }

    public WidowUpgradeManagerAction(final AbstractPlayer p, final boolean random) {
        this(p, random, 1, false);
    }

    public WidowUpgradeManagerAction(final AbstractPlayer p, final boolean random, final int amount) {
        this(p,random, amount, false);
    }

    public WidowUpgradeManagerAction(final AbstractPlayer p, final boolean random, final int amount, final boolean permanent) {
        this.p = p;
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
            for (AbstractCard c : p.hand.group) {
                if (!c.canUpgrade())
                    cannotUpgrade.add(c);
            }
            if (cannotUpgrade.size() == p.hand.size()) {
                isDone = true;
                return;
            }
            if (p.hand.size() - cannotUpgrade.size() <= amount) {
                for (AbstractCard c : p.hand.group) {
                    if (c.canUpgrade())
                        addToTop(new WidowUpgradeCardAction(permanent, c));
                }
                isDone = true;
                return;
            }
            if (random) {
                CardGroup cardsToUpgrade = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard c : p.hand.group)
                    if (c.canUpgrade())
                        cardsToUpgrade.addToTop(c);
                for (int i=0; i<amount && !cardsToUpgrade.isEmpty(); i++) {
                    AbstractCard c = cardsToUpgrade.getRandomCard(AbstractDungeon.cardRandomRng);
                    if (c.canUpgrade())
                        addToBot(new WidowUpgradeCardAction(permanent, c));
                    cardsToUpgrade.removeCard(c);
                }
                isDone = true;
                return;
            }

            p.hand.group.removeAll(cannotUpgrade);
            if (p.hand.size() <= amount) {
                for (AbstractCard c : p.hand.group)
                    addToTop(new WidowUpgradeCardAction(permanent, c));
                returnCards();
                isDone = true;
            }
            if (p.hand.size() > amount) {
                AbstractDungeon.handCardSelectScreen.open("Upgrade", amount, false, false, false, true);
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                addToTop(new WidowUpgradeCardAction(permanent, c));
                p.hand.addToTop(c);
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
            p.hand.addToTop(c);
        p.hand.refreshHandLayout();
    }
}
