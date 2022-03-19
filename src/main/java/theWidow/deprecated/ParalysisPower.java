package theWidow.deprecated;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ReactivePower;
import theWidow.WidowMod;
import theWidow.powers.AbstractEasyPower;
import theWidow.util.Wiz;


public class ParalysisPower extends AbstractEasyPower implements CloneablePowerInterface {

    public static final String POWER_ID = WidowMod.makeID(ParalysisPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public boolean primed;

    public ParalysisPower(final AbstractCreature owner, final int amount) {
        super(powerStrings.NAME, PowerType.DEBUFF, owner, amount);
        primed = false;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void onInitialApplication() {
        if (owner instanceof AbstractMonster && !owner.isDying) {
            ((AbstractMonster) owner).createIntent();
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (owner.hasPower(ReactivePower.POWER_ID) && !owner.isDying)
            ((AbstractMonster) owner).createIntent();
        return damageAmount;
    }

    @Override
    public void onRemove() {
        if (owner instanceof AbstractMonster && !owner.isDying) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    ((AbstractMonster) owner).createIntent();
                    isDone = true;
                }
            });
        }
    }

    @Override
    public void atEndOfRound() {
        if (amount <= 1)
            addToBot(new RemoveSpecificPowerAction(owner, Wiz.adp(), this));
        else {
            addToBot(new ReducePowerAction(owner, Wiz.adp(), this, 1));
            primed = false;
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new ParalysisPower(owner, amount);
    }
}
