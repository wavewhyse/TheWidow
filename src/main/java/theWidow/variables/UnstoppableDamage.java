package theWidow.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import theWidow.cards.Unstoppable;

import static theWidow.WidowMod.makeID;

public class UnstoppableDamage extends DynamicVariable {
    @Override
    public String key() {
        return makeID("UnstoppableD");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        for (AbstractPower pow : AbstractDungeon.player.powers)
            switch (pow.ID) {
                case WeakPower.POWER_ID:
                case VulnerablePower.POWER_ID:
                case FrailPower.POWER_ID:
                    return true;
            }
        return card.isDamageModified;
    }

    @Override
    public int value(AbstractCard card) {
        float dam = card.damage;
        if (AbstractDungeon.player.hasPower(WeakPower.POWER_ID))
            dam /= 0.75f;
        for (AbstractPower pow : AbstractDungeon.player.powers)
            switch (pow.ID) {
                case WeakPower.POWER_ID:
                case VulnerablePower.POWER_ID:
                case FrailPower.POWER_ID:
                    dam += pow.amount * (Unstoppable.SCALING + (card.upgraded? Unstoppable.UPGRADE_PLUS_SCALING : 0));
                    break;
            }
        return (int) dam; // KNOWN BUG: additional damage from cleanse will not scale in the preview with Vulnerable or other multipliers
    }

    @Override
    public int baseValue(AbstractCard card) {
        return card.damage;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
       return card.upgradedDamage;
    }
}