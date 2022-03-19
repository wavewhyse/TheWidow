package theWidow.cards.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import theWidow.WidowMod;
import theWidow.util.Wiz;

import java.util.ArrayList;

import static theWidow.WidowMod.makeCardPath;

public class Alloy extends ModuleXOption {
    public static final String ID = WidowMod.makeID(Alloy.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("MoXAlloy");

    public Alloy() {
        this(new ArrayList<>(), 0);
    }

    public Alloy(ArrayList<AbstractCard> unchosenOptions, int remainingChoices) {
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
        Wiz.apply(new MetallicizePower(p, magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Alloy();
    }
}
