package theWidow.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class ImprobabilityDriveAction extends AbstractGameAction {

    private static final float DURATION = Settings.ACTION_DUR_FAST;

    private AbstractPlayer p;
    private int energyOnUse = -1;
    private boolean freeToPlayOnce = false;

    public ImprobabilityDriveAction(AbstractPlayer p, int energyOnUse, boolean freeToPlayOnce) {
        this.p = p;
        actionType = ActionType.ENERGY;
        duration = DURATION;
        this.energyOnUse = energyOnUse;
        this.freeToPlayOnce = freeToPlayOnce;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (energyOnUse != -1)
            effect = energyOnUse;
        if (p.hasRelic(ChemicalX.ID)) {
            effect += ChemicalX.BOOST;
            p.getRelic(ChemicalX.ID).flash();
        }
        addToBot(new DrawCardAction(effect));
        if (!this.freeToPlayOnce)
            p.energy.use(EnergyPanel.totalCount);
        isDone = true;
    }
}