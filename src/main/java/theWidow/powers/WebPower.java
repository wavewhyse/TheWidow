package theWidow.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.util.Wiz;

public class WebPower extends AbstractEasyPower implements CloneablePowerInterface {
    public static final String POWER_ID = WidowMod.makeID(WebPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public WebPower(final AbstractCreature owner, final int amount) {
        super( powerStrings.NAME,
                    PowerType.BUFF,
                    owner,
                    amount );
    }

    @Override
    public void updateDescription() {
        description = String.format(powerStrings.DESCRIPTIONS[0], amount);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            flashWithoutSound();
                Wiz.adam().addToTop(new ReducePowerAction(owner, owner, this, 1));
            Wiz.applyTop(new CaughtPower(info.owner, amount));
        }
        return damageAmount;
    }

    @Override
    public AbstractPower makeCopy() {
        return new WebPower(owner, amount);
    }
}
