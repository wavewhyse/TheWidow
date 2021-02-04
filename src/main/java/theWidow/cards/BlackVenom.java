package theWidow.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import theWidow.WidowMod;
import theWidow.characters.TheWidow;
import theWidow.powers.NecrosisPower;
import theWidow.powers.ParalysisPower;

import static theWidow.WidowMod.makeCardPath;

public class BlackVenom extends ExtraMagicalCustomCard  implements Downgradeable {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(BlackVenom.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("BlackVenom.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int STATUSES = 2;
    private static final int UPGRADE_PLUS_STATUSES = 1;
    private static final int NECROSIS = 4;
    private static final int UPGRADE_PLUS_NECROSIS = 2;

    // /STAT DECLARATION/

    public BlackVenom() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = STATUSES;
        secondMagicNumber = baseSecondMagicNumber = NECROSIS;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
        addToBot(new ApplyPowerAction(m, p, new ParalysisPower(m, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(m, p, new NecrosisPower(m, secondMagicNumber), secondMagicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_STATUSES);
            upgradeSecondMagicNumber(UPGRADE_PLUS_NECROSIS);
            initializeDescription();
        }
    }

    @Override
    public void downgrade() {
        if (upgraded) {
            name = cardStrings.NAME;
            timesUpgraded--;
            upgraded = false;
            magicNumber = baseMagicNumber = STATUSES;
            secondMagicNumber = baseSecondMagicNumber = NECROSIS;
            upgradedMagicNumber = upgradedSecondMagicNumber = false;
        }
    }
}
