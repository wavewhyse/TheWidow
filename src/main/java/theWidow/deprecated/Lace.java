package theWidow.deprecated;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.cards.ExtraMagicalCustomCard;
import theWidow.powers.WebPower;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
@Deprecated
public class Lace extends ExtraMagicalCustomCard {

    public static final String ID = WidowMod.makeID(Lace.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Lace.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 0;
    private static final int WEB = 4;
    private static final int VULNERABLE = 2;
    private static final int UPGRADE_PLUS_WEB = 2;

    public Lace() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = WEB;
        secondMagicNumber = baseSecondMagicNumber = VULNERABLE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new WebPower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new VulnerablePower(p, secondMagicNumber, false), secondMagicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_WEB);
            initializeDescription();
        }
    }
}
