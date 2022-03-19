package theWidow.cards.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import theWidow.WidowMod;
import theWidow.util.Wiz;

import java.util.ArrayList;

import static theWidow.WidowMod.makeCardPath;

public class Coordinate extends ModuleXOption {
    public static final String ID = WidowMod.makeID(Coordinate.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("MoXCoordinate");

    public Coordinate() {
        this(new ArrayList<>(), 0);
    }

    public Coordinate(ArrayList<AbstractCard> unchosenOptions, int remainingChoices) {
        super(unchosenOptions, remainingChoices,
                ID,
                cardStrings.NAME,
                IMG,
                cardStrings.DESCRIPTION
        );
        magicNumber = baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.apply(new DexterityPower(p, magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Coordinate();
    }
}
