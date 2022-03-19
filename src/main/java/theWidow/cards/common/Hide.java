package theWidow.cards.common;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.WebPower;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class Hide extends CustomCard {
    public static final String ID = WidowMod.makeID(Hide.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Hide() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Hide.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.COMMON,
                CardTarget.SELF );
        baseBlock = 8;
        magicNumber = baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        Wiz.apply(new WebPower(p, magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(2);
            upgradeMagicNumber(2);
        }
    }
}
