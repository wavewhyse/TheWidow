package theWidow.deprecated;

import basemod.AutoAdd;
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

@AutoAdd.Ignore
public class BlackVenom2 extends CustomCard {
    public static final String ID = WidowMod.makeID(BlackVenom2.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int MULT = 2;
    private static final int UPGRADE_PLUS_MULT = 1;



    public BlackVenom2() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(BlackVenom2.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.ENEMY );
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