package theWidow.cards;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.TheWidow;
import theWidow.WidowMod;

import static theWidow.WidowMod.makeCardPath;

public class BlackVenom2 extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(BlackVenom2.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("BlackVenom.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int MULT = 2;
    private static final int UPGRADE_PLUS_MULT = 1;

    // /STAT DECLARATION/

    public BlackVenom2() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MULT;
        exhaust = true;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractPower pow : m.powers) {
            pow.flashWithoutSound();
            if (pow.type == AbstractPower.PowerType.DEBUFF) {
                if (pow instanceof CloneablePowerInterface) {
                    for (int i = 1; i < magicNumber; i++)
                        addToBot(new ApplyPowerAction(m, p, ((CloneablePowerInterface) pow).makeCopy(), pow.amount, true));
                    addToBot(new WaitAction(0.15f));
                } else {
                    addToBot(new AbstractGameAction() {
                        @Override
                        public void update() {
                            pow.amount *= MULT;
                        }
                    });
                }
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MULT);
            initializeDescription();
        }
    }
}
