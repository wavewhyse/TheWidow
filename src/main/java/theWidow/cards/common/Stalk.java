package theWidow.cards.common;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import theWidow.WidowMod;
import theWidow.util.Wiz;

public class Stalk extends CustomCard {
    public static final String ID = WidowMod.makeID(Stalk.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Stalk() {
        super( ID,
                cardStrings.NAME,
                WidowMod.makeCardPath("Ambush"),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                theWidow.TheWidow.Enums.COLOR_BLACK,
                CardRarity.COMMON,
                CardTarget.ENEMY );
        baseBlock = 8;
        magicNumber = baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        Wiz.apply(new VulnerablePower(m, magicNumber, false));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(3);
            upgradeMagicNumber(1);
        }
    }
}
