package theWidow.deprecated;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.WidowMod;
import theWidow.cards.BetaCard;
import theWidow.characters.TheWidow;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
@Deprecated
public class ImprobabilityDrive extends BetaCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(ImprobabilityDrive.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("ImprobabilityDrive.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;

    // /STAT DECLARATION/

    public ImprobabilityDrive() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = 1;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(magicNumber));
        addToBot(new DrawCardAction(magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        upgradeMagicNumber(1);
        timesUpgraded++;
        upgraded = true;
        name = cardStrings.NAME + "+" + timesUpgraded;
        initializeTitle();
        initializeDescription();
    }

    @Override
    public void downgrade() {
        if (upgraded) {
            magicNumber = baseMagicNumber = baseMagicNumber - 1;
                    timesUpgraded--;
            if (timesUpgraded == 0) {
                upgraded = false;
                upgradedMagicNumber = false;
                name = cardStrings.NAME;
            } else
                name = cardStrings.NAME + "+" + timesUpgraded;
            initializeTitle();
            initializeDescription();
        }
    }
}
