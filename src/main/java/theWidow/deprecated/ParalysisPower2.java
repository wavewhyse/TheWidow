package theWidow.deprecated;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.powers.AbstractEasyPower;


public class ParalysisPower2 extends AbstractEasyPower implements CloneablePowerInterface {
    public static final String POWER_ID = WidowMod.makeID(ParalysisPower2.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ParalysisPower2(final AbstractCreature owner, final int amount) {
        super( powerStrings.NAME,
                PowerType.DEBUFF,
                owner,
                amount );
    }

    @Override
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= 10)
            addToBot(new ReducePowerAction(owner, owner, POWER_ID, 10));
            addToBot(new StunMonsterAction((AbstractMonster) owner, owner));
    }

    @Override
    public AbstractPower makeCopy() {
        return new ParalysisPower2(owner, amount);
    }
}
