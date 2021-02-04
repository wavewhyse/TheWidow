package theWidow.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theWidow.vfx.UpgradeHammerHit;

import java.util.ArrayList;

public class InspirationAction extends AbstractGameAction {

    private static final float DURATION = Settings.ACTION_DUR_LONG;

    private ArrayList<AbstractCard> cannotUpgrade = new ArrayList<>();
    private AbstractPlayer p;
    private final int energyOnUse;
    private final boolean freeToPlayOnce;
    private final boolean upgraded;
    private int effect;

    public InspirationAction(int energyOnUse, boolean freeToPlayOnce, boolean upgraded) {
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
            effect = EnergyPanel.totalCount;
            if (energyOnUse != -1)
                effect = energyOnUse;
            if (p.hasRelic(ChemicalX.ID)) {
                effect += ChemicalX.BOOST;
                p.getRelic(ChemicalX.ID).flash();
            }
            if(upgraded)
                effect++;
            if (effect == 0) {
                if (!this.freeToPlayOnce)
                    p.energy.use(EnergyPanel.totalCount);
                isDone = true;
                return;
            }

            for (AbstractCard c : p.hand.group) {
                if (!c.canUpgrade())
                    cannotUpgrade.add(c);
            }
            if (cannotUpgrade.size() == p.hand.size()) {
                if (!this.freeToPlayOnce)
                    p.energy.use(EnergyPanel.totalCount);
                isDone = true;
                return;
            }
            if (p.hand.size() - cannotUpgrade.size() <= 1) {
                for (AbstractCard c : p.hand.group) {
                    doUpgrades(c);
                }
                if (!this.freeToPlayOnce)
                    p.energy.use(EnergyPanel.totalCount);
                isDone = true;
                return;
            }

            p.hand.group.removeAll(cannotUpgrade);
            if (p.hand.size() > 1) {
                AbstractDungeon.handCardSelectScreen.open("upgrade", 1, false, false, false, true);
                tickDuration();
                return;
            }
            if (p.hand.size() <= 1) {
                for (AbstractCard c : p.hand.group)
                    doUpgrades(c);
                returnCards();
                if (!this.freeToPlayOnce)
                    p.energy.use(EnergyPanel.totalCount);
                isDone = true;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                doUpgrades(c);
                p.hand.addToTop(c);
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

    private void doUpgrades(AbstractCard c) {
        for (int i = 0; i < effect && c.canUpgrade(); i++) {
            c.upgrade();
            AbstractDungeon.effectsQueue.add(new UpgradeHammerHit(c));
        }
        c.costForTurn = 0;
        c.applyPowers();
    }

    private void returnCards() {
        for (AbstractCard c : cannotUpgrade)
            p.hand.addToTop(c);
        p.hand.refreshHandLayout();
    }
}
