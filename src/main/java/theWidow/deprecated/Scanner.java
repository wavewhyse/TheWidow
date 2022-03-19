package theWidow.deprecated;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.cards.BetaCard;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
@Deprecated
public class Scanner extends BetaCard {
    public static final String ID = WidowMod.makeID(Scanner.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int SCRY = 4;
    private static final int UPGRADE_PLUS_SCRY = 2;



    public Scanner() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Scanner.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.SELF,
                cardStrings );
        magicNumber = baseMagicNumber = SCRY;
        isInnate = true;
        exhaust = true;
        SewingKitCheck();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ScryAction(magicNumber));
    }

    @Override
    public void upgrade() {
        upgradeMagicNumber(UPGRADE_PLUS_SCRY);
        upgradeName();
    }

    @Override
    public void downgrade() {
        baseMagicNumber -= UPGRADE_PLUS_SCRY;
        super.downgrade();
    }
}
