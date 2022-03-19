package theWidow.deprecated;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import theWidow.WidowMod;
import theWidow.powers.AbstractEasyPower;
import theWidow.util.Wiz;

public class ShockedPower extends AbstractEasyPower implements CloneablePowerInterface {

    public static final String POWER_ID = WidowMod.makeID(ShockedPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ShockedPower(AbstractCreature owner, int amount) {
        super( powerStrings.NAME,
                PowerType.DEBUFF,
                owner,
                amount );
        isTurnBased = true;
    }

    @Override
    public void atEndOfRound() {
        if (amount <= 1)
            addToBot(new RemoveSpecificPowerAction(owner, Wiz.adp(), this));
        else
            addToBot(new ReducePowerAction(owner, Wiz.adp(), this, 1));
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            flashWithoutSound();
            addToTop(new DamageAction(owner, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE, true));
            addToTop(new VFXAction(new LightningEffect(owner.drawX, owner.drawY), 0.1F));
            //addToTop(new SFXAction("ORB_LIGHTNING_EVOKE",3.0f));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(powerStrings.DESCRIPTIONS[0], amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new ShockedPower(owner, amount);
    }
}
