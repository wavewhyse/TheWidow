package theWidow.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
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
        int oldDamage = card.baseDamage;
        for (AbstractPower pow : AbstractDungeon.player.powers)
            switch (pow.ID) {
                case WeakPower.POWER_ID:
                case VulnerablePower.POWER_ID:
                case FrailPower.POWER_ID:
                    card.baseDamage += pow.amount * (Unstoppable.SCALING + (card.upgraded? Unstoppable.UPGRADE_PLUS_SCALING : 0));
                    break;
            }
        AbstractMonster target = null;
        for (AbstractMonster m: AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m.hb.hovered) {
                target = m;
                break;
            }
        }
        card.calculateCardDamage(target);
        float finalDamage = card.damage;
        if (AbstractDungeon.player.hasPower(WeakPower.POWER_ID))
            finalDamage /= 0.75f;
        card.baseDamage = oldDamage;
        //card.calculateCardDamage(target);
        return (int) finalDamage;
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