package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import theWidow.TheWidow;
import theWidow.WidowMod;

import static theWidow.WidowMod.makeCardPath;

public class Underfoot extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Underfoot.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Underfoot.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF_AND_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int BLOCK = 7;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int EXTEND = 3;
    private static final int UPGRADE_PLUS_EXTEND = 1;

    // /STAT DECLARATION/

    public Underfoot() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        magicNumber = baseMagicNumber = EXTEND;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot( new GainBlockAction(p, block));
        if (m.hasPower(WeakPower.POWER_ID))
            addToBot( new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
        if (m.hasPower(VulnerablePower.POWER_ID))
            addToBot( new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_EXTEND);
            initializeDescription();
        }
    }
}
