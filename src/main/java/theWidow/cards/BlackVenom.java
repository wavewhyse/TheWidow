package theWidow.cards;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.SapPower;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
public class BlackVenom extends ExtraExtraMagicalCustomCard {

    public static final String ID = WidowMod.makeID(BlackVenom.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("BlackVenom.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int STATUSES = 3;
    private static final int UPGRADE_PLUS_STATUSES = 1;
    private static final int SAP = 4;
    private static final int UPGRADE_PLUS_SAP = 2;
    private static final int STRENGTH_DOWN = 1;
    private static final int UPGRADE_PLUS_STRENGTH_DOWN = 1;

    public BlackVenom() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = STATUSES;
        secondMagicNumber = baseSecondMagicNumber = SAP;
        thirdMagicNumber = baseThirdMagicNumber = STRENGTH_DOWN;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));
        addToBot(new ApplyPowerAction(m, p, new SapPower(m, secondMagicNumber), secondMagicNumber));
        addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -thirdMagicNumber), -thirdMagicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_STATUSES);
            upgradeSecondMagicNumber(UPGRADE_PLUS_SAP);
            upgradeThirdMagicNumber(UPGRADE_PLUS_STRENGTH_DOWN);
            initializeDescription();
        }
    }
}
