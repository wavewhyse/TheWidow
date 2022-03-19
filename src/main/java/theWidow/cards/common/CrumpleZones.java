package theWidow.cards.common;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class CrumpleZones extends CustomCard {

    public static final String ID = WidowMod.makeID(CrumpleZones.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public CrumpleZones() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(CrumpleZones.class.getSimpleName()),
                0,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.COMMON,
                CardTarget.SELF );
        baseBlock = 12;
        magicNumber = baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        Wiz.apply(new FrailPower(p, magicNumber, false));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(4);
        }
    }
}
