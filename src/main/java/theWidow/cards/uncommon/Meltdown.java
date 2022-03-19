package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.EasyChooseCardAction;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class Meltdown extends CustomCard {
    public static final String ID = WidowMod.makeID(Meltdown.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final UIStrings uistrings = CardCrawlGame.languagePack.getUIString(ID);

    public Meltdown() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Meltdown.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.SELF );
        magicNumber = baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        addToBot(new MeltdownAction(magicNumber));
        addToBot(new EasyChooseCardAction(1,
                card -> card.upgraded, uistrings.TEXT[0],
                (card, params) -> {
                    for (int i = 0; i < (int) params[0]; i++)
                        addToTop(new MakeTempCardInHandAction(card.makeCopy()));
                    Wiz.adp().hand.moveToExhaustPile(card);
                    return true;
                },
                magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
}
