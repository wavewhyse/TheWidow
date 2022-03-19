package theWidow.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ReducePowerByHalfAction extends AbstractGameAction {
    private final AbstractPower power;

    public ReducePowerByHalfAction(AbstractPower power) {
        this.power = power;
    }

    @Override
    public void update() {
        if (power.amount == 1)
            addToTop(new RemoveSpecificPowerAction(power.owner, power.owner, power));
        else {
            power.reducePower(power.amount / 2);
            AbstractDungeon.onModifyPower();
        }
        isDone = true;
    }
}
