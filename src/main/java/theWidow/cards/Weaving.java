package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.WidowMod;
import theWidow.actions.UpgradeDrawnCardAction;
import theWidow.characters.TheWidow;

import static theWidow.WidowMod.makeCardPath;

public class Weaving extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Weaving.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("Weaving.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DRAW = 1;
    //private static final int UPGRADE_PLUS_DRAW = 1;

    // /STAT DECLARATION/

    public Weaving() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        //magicNumber = baseMagicNumber = DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) { //TODO: make this more flexible with full hands, & smartly choosing un-upgraded cards
        boolean upgradeFirst = AbstractDungeon.cardRandomRng.randomBoolean();
        addToBot(new DrawCardAction(1));
        if (upgradeFirst || upgraded)
            addToBot(new UpgradeDrawnCardAction());
        addToBot(new DrawCardAction(1));
        if (!upgradeFirst || upgraded)
            addToBot(new UpgradeDrawnCardAction());
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
