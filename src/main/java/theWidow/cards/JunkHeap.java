package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import theWidow.TheWidow;
import theWidow.WidowMod;

import static theWidow.WidowMod.makeCardPath;

public class JunkHeap extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(JunkHeap.class.getSimpleName());
    public static final String IMG = makeCardPath("JunkHeap.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 0;
    private static final int BLOCK = 9;
    private static final int UPGRADE_PLUS_BLOCK = 4;
    private static final int FRAIL = 2;

    // /STAT DECLARATION/

    public JunkHeap() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        magicNumber = baseMagicNumber = FRAIL;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        addToBot(new ApplyPowerAction(p, p, new FrailPower(p, magicNumber, false), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
