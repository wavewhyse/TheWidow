package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.WidowMod;
import theWidow.characters.TheWidow;
import theWidow.powers.SadisticIntentPower;

import static theWidow.WidowMod.makeCardPath;

public class SadisticIntent extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(SadisticIntent.class.getSimpleName());
    public static final String IMG = makeCardPath("SadisticIntent.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;
    //private static final int STATS = 1;
    //private static final int UPGRADE_PLUS_STATS = 1;

    // /STAT DECLARATION/

    public SadisticIntent() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        //magicNumber = baseMagicNumber = STATS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot( new ApplyPowerAction(p, p, new SadisticIntentPower(p, 1), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeMagicNumber(UPGRADE_PLUS_STATS);
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}
