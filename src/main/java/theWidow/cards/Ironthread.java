package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.WidowMod;
import theWidow.actions.WidowAllPurposeUpgradeAction;
import theWidow.characters.TheWidow;

import static theWidow.WidowMod.makeCardPath;

public class Ironthread extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Ironthread.class.getSimpleName());
    public static final String IMG = makeCardPath("Ironthread.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 0;
    //private static final int BLOCK = 2;
    //private static final int UPGRADE_PLUS_BLOCK = 2;
    private static final int UPGRADES = 1;
    private static final int UPGRADE_PLUS_UPGRADES = 1;

    // /STAT DECLARATION/

    public Ironthread() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        //baseBlock = BLOCK;
        magicNumber = baseMagicNumber = UPGRADES;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //addToBot(new GainBlockAction(p, block));
        addToBot(new WidowAllPurposeUpgradeAction(p, magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_UPGRADES);
            initializeDescription();
        }
    }
}