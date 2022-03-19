package theWidow.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.function.BiFunction;

public class EasyXCostAction extends AbstractGameAction {
    public BiFunction<Integer, Object[], Boolean> xActionUpdate;
    public Object[] params;
    protected int baseValue;
    protected boolean freeToPlayOnce;
    protected int effect;

    /**
     * @param card          The card played. Usually should simply be "this".
     * @param xActionUpdate A BiFunction that receives an integer for the energy amount (includes Chem X) and any number of integer parameters in the form of an array. The return value of this function is isDone.
     * @param params        Any number of integer parameters. These will be passed to the update function to avoid possible value changes between the creation of this action and when it is updated.
     */
    public EasyXCostAction(AbstractCard card, BiFunction<Integer, Object[], Boolean> xActionUpdate, Object... params) {
        this.baseValue = card.energyOnUse;
        this.freeToPlayOnce = card.freeToPlayOnce;
        this.xActionUpdate = xActionUpdate;
        this.params = params;
        duration = startDuration = 0.1f;
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            effect = EnergyPanel.totalCount;
            if (this.baseValue != -1) {
                effect = this.baseValue;
            }

            if (AbstractDungeon.player.hasRelic(ChemicalX.ID)) {
                effect += 2;
                AbstractDungeon.player.getRelic(ChemicalX.ID).flash();
            }

            isDone = xActionUpdate.apply(effect, params);
            tickDuration();

            if (!this.freeToPlayOnce) {
                AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
            }
        } else {
            isDone = xActionUpdate.apply(effect, params);
        }
    }
}