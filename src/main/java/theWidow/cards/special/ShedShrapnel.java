package theWidow.cards.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import theWidow.WidowMod;
import theWidow.util.Wiz;

import java.util.ArrayList;

import static theWidow.WidowMod.makeCardPath;

public class ShedShrapnel extends ModuleXOption {
    public static final String ID = WidowMod.makeID(ShedShrapnel.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("MoXShedShrapnel");

    public ShedShrapnel() {
        this(new ArrayList<>(), 0);
    }

    public ShedShrapnel(ArrayList<AbstractCard> unchosenOptions, int remainingChoices) {
        super(unchosenOptions, remainingChoices,
                ID,
                cardStrings.NAME,
                IMG,
                cardStrings.DESCRIPTION
        );
        magicNumber = baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.apply(new ThornsPower(p, magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShedShrapnel();
    }
}