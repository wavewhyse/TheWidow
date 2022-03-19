package theWidow.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.actions.ReducePowerByHalfAction;
import theWidow.cards.common.Fangs;


public class CaughtPower extends AbstractEasyPower implements CloneablePowerInterface {
    public static final String POWER_ID = WidowMod.makeID(CaughtPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public CaughtPower(final AbstractCreature owner, final int amount) {
        super( powerStrings.NAME,
                PowerType.DEBUFF,
                owner,
                amount );
    }

    @Override
    public void updateDescription() {
        description = String.format(powerStrings.DESCRIPTIONS[0], amount);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL)
            return damage + amount;
        else return damage;
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && !Fangs.ID.equals(info.name))
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        return damageAmount;
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new ReducePowerByHalfAction(this));
        flashWithoutSound();
    }

    @Override
    public AbstractPower makeCopy() {
        return new CaughtPower(owner, amount);
    }
}
