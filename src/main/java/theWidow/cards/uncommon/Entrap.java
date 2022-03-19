package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.CaughtPower;
import theWidow.powers.SapPower;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class Entrap extends CustomCard {
    public static final String ID = WidowMod.makeID(Entrap.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Entrap() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Entrap.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.ENEMY );
        magicNumber = baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.apply(new SapPower(m, magicNumber));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int count = Wiz.count(m.powers, pow -> pow.type == AbstractPower.PowerType.DEBUFF);
                if (count > 0)
                    Wiz.applyTop(new CaughtPower(m, count*2));
                isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
}
