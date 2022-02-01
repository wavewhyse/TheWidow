package theWidow.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theWidow.cards.ModuleX;

import static theWidow.WidowMod.makeID;

public class XUpgradesLeft extends DynamicVariable {

    @Override
    public String key() {
        return makeID("MXU");
    }

    @Override
    public boolean isModified(AbstractCard card) { return card.upgraded; }

    @Override
    public int value(AbstractCard card) {
        return ModuleX.MAX_UPGRADES - card.timesUpgraded;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ModuleX.MAX_UPGRADES - card.timesUpgraded;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}