package theWidow.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.actions.ReducePowerByHalfAction;


public class SapPower extends AbstractEasyPower implements CloneablePowerInterface {
    public static final String POWER_ID = WidowMod.makeID(SapPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SapPower(final AbstractCreature owner, final int amount) {
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
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return damage - amount;
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL)
            addToBot(new ReducePowerByHalfAction(this));
        return damageAmount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer)
            addToBot(new ReducePowerByHalfAction(this));
    }

    @Override
    public AbstractPower makeCopy() {
        return new SapPower(owner, amount);
    }

}
