package theWidow.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theWidow.cards.ExtraExtraMagicalCustomCard;

import static theWidow.WidowMod.makeID;

public class thirdMagicNumber extends DynamicVariable {

    @Override
    public String key() {
        return makeID("M3");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((ExtraExtraMagicalCustomCard) card).isThirdMagicNumberModified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((ExtraExtraMagicalCustomCard) card).thirdMagicNumber;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((ExtraExtraMagicalCustomCard) card).baseThirdMagicNumber;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((ExtraExtraMagicalCustomCard) card).upgradedThirdMagicNumber;
    }
}