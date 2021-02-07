package theWidow.deprecated.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theWidow.potions.DistilledCardPotion;

import java.util.ArrayList;

@Deprecated
public class Distill2Action extends AbstractGameAction {

    private static final float DURATION = Settings.ACTION_DUR_FAST;

    private AbstractPlayer p;
    private ArrayList<AbstractCard> unpotionable = new ArrayList<>();
    private int energyOnUse = -1;
    private boolean freeToPlayOnce = false;
    private boolean upgraded = false;

    public Distill2Action(int energyOnUse, boolean freeToPlayOnce, boolean upgraded) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = DURATION;
        p = AbstractDungeon.player;
        this.energyOnUse = energyOnUse;
        this.freeToPlayOnce = freeToPlayOnce;
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        if (duration == DURATION) {
            int effect = EnergyPanel.totalCount;
            if (energyOnUse != -1)
                effect = energyOnUse;
            if (p.hasRelic(ChemicalX.ID)) {
                effect += ChemicalX.BOOST;
                p.getRelic(ChemicalX.ID).flash();
            }
            if(upgraded)
                effect++;
            for (AbstractCard c : p.hand.group) {
                if (c.costForTurn > effect || c.type == AbstractCard.CardType.STATUS || c.type == AbstractCard.CardType.CURSE)
                    unpotionable.add(c);
            }
            if (unpotionable.size() == p.hand.size()) {
                if (!this.freeToPlayOnce)
                    p.energy.use(EnergyPanel.totalCount);
                isDone = true;
                return;
            }

            if (p.hand.size() - unpotionable.size() == 1) {
                for (AbstractCard c : p.hand.group) {
                    if (c.costForTurn > effect || !c.canUse(AbstractDungeon.player, null))
                        continue;
                    doDistill(c);
                    if (!this.freeToPlayOnce)
                        p.energy.use(EnergyPanel.totalCount);
                    isDone = true;
                    return;
                }
            }
            p.hand.group.removeAll(unpotionable);
            if (p.hand.size() > 1) {
                AbstractDungeon.handCardSelectScreen.open("distill", 1, false, false, false, false);
                tickDuration();
                return;
            }
            if (p.hand.size() == 1) {
                doDistill(p.hand.getTopCard());
                returnCards();
                if (!this.freeToPlayOnce)
                    p.energy.use(EnergyPanel.totalCount);
                isDone = true;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                doDistill(c);
            }
            returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            if (!this.freeToPlayOnce)
                p.energy.use(EnergyPanel.totalCount);
            isDone = true;
        }
        tickDuration();
    }

    private void doDistill(AbstractCard c) {
        AbstractCard cc = c.makeSameInstanceOf();
        cc.energyOnUse = energyOnUse;
        cc.freeToPlayOnce = true;
        cc.purgeOnUse = true;
        addToBot(new ObtainPotionAction(new DistilledCardPotion(cc)));
        p.hand.moveToExhaustPile(c);
    }

    private void returnCards() {
        for (AbstractCard c : unpotionable)
            p.hand.addToTop(c);
        p.hand.refreshHandLayout();
    }
}
