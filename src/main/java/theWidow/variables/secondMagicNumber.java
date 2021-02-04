package theWidow.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theWidow.cards.ExtraMagicalCustomCard;

import static theWidow.WidowMod.makeID;

public class secondMagicNumber extends DynamicVariable {

    @Override
    public String key() {
        return makeID("M2");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((ExtraMagicalCustomCard) card).isSecondMagicNumberModified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((ExtraMagicalCustomCard) card).secondMagicNumber;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((ExtraMagicalCustomCard) card).baseSecondMagicNumber;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((ExtraMagicalCustomCard) card).upgradedSecondMagicNumber;
    }
}