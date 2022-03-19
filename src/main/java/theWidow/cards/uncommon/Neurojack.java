package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeManagerAction;
import theWidow.powers.AbstractEasyPower;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class Neurojack extends CustomCard {
    public static final String ID = WidowMod.makeID(Neurojack.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Neurojack() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Neurojack.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.POWER,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.SELF );
        magicNumber = baseMagicNumber = 1;
    }
    
    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        Wiz.apply(new NeurojackPower(p, magicNumber));
    }

    public static class NeurojackPower extends AbstractEasyPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(NeurojackPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        public NeurojackPower(AbstractCreature owner, int amount) {
            super( powerStrings.NAME,
                    AbstractPower.PowerType.BUFF,
                    owner,
                    amount );
        }

        @Override
        public void atStartOfTurnPostDraw() {
            flash();
            addToBot(new WidowUpgradeManagerAction(amount, true));
        }

        @Override
        public void updateDescription() {
            if (amount == 1)
                description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
            else
                description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[2];
        }

        @Override
        public AbstractPower makeCopy() {
            return new NeurojackPower(owner, amount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
}