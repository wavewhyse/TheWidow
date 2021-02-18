package theWidow.deprecated;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
@Deprecated
public class WebSwing extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(WebSwing.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;

    private static final int BLOCK = 12;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    private static final int DRAW = 2;
    private static final int UPGRADE_PLUS_DRAW = 1;

    // /STAT DECLARATION/


    public WebSwing() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        magicNumber = baseMagicNumber = DRAW;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new GainBlockAction(p, block));
        addToBot(
                new DrawCardAction(magicNumber));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            initializeDescription();
        }
    }
}