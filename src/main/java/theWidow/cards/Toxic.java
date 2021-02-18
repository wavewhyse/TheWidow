package theWidow.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.NecrosisPower;

import static theWidow.WidowMod.makeCardPath;

public class Toxic extends ExtraMagicalCustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Toxic.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("Toxic.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 0;
    private static final int DEBUFFS = 2;
    private static final int UPGRADE_PLUS_DEBUFFS = 1;
    private static final int NECROSIS = 2;
    private static final int UPGRADE_PLUS_NECROSIS = 1;

    // /STAT DECLARATION/

    public Toxic() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DEBUFFS;
        secondMagicNumber = baseSecondMagicNumber = NECROSIS;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot( new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
        addToBot( new ApplyPowerAction(m, p, new NecrosisPower(m, secondMagicNumber), secondMagicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DEBUFFS);
            upgradeSecondMagicNumber(UPGRADE_PLUS_NECROSIS);
        }
    }

    @Override
    public void downgrade() {
        if (upgraded) {
            name = cardStrings.NAME;
            timesUpgraded--;
            upgraded = false;
            magicNumber = baseMagicNumber = DEBUFFS;
            secondMagicNumber = baseSecondMagicNumber = NECROSIS;
            upgradedMagicNumber = upgradedSecondMagicNumber = false;
        }
    }
}
