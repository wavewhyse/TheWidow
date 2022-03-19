package theWidow.deprecated;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.cards.ExtraExtraMagicalCustomCard;
import theWidow.powers.SapPower;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
public class BlackVenom extends ExtraExtraMagicalCustomCard {
    public static final String ID = WidowMod.makeID(BlackVenom.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int STATUSES = 3;
    private static final int UPGRADE_PLUS_STATUSES = 1;
    private static final int SAP = 4;
    private static final int UPGRADE_PLUS_SAP = 2;
    private static final int STRENGTH_DOWN = 1;
    private static final int UPGRADE_PLUS_STRENGTH_DOWN = 1;



    public BlackVenom() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(BlackVenom.class.getSimpleName()),
                2,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.RARE,
                CardTarget.ENEMY );
        magicNumber = baseMagicNumber = STATUSES;
        secondMagicNumber = baseSecondMagicNumber = SAP;
        thirdMagicNumber = baseThirdMagicNumber = STRENGTH_DOWN;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.apply(new WeakPower(m, magicNumber, false));
        Wiz.apply(new VulnerablePower(m, magicNumber, false));
        Wiz.apply(new SapPower(m, secondMagicNumber));
        Wiz.apply(new StrengthPower(m, -thirdMagicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_STATUSES);
            upgradeSecondMagicNumber(UPGRADE_PLUS_SAP);
            upgradeThirdMagicNumber(UPGRADE_PLUS_STRENGTH_DOWN);
            initializeDescription();
        }
    }
}
