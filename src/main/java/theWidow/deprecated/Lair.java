package theWidow.deprecated;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.AbstractEasyPower;
import theWidow.powers.WebPower;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
@Deprecated
public class Lair extends CustomCard {
    public static final String ID = WidowMod.makeID(Lair.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int WEB = 2;
    private static final int UPGRADE_PLUS_WEB = 1;



    public Lair() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Lair.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.POWER,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.SELF );
        magicNumber = baseMagicNumber = WEB;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.apply(new LairPower(p, magicNumber));
    }

    public static class LairPower extends AbstractEasyPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(LairPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        public LairPower(final AbstractCreature owner, final int amount) {
super( powerStrings.NAME,
                    PowerType.BUFF,
                    owner,
                    amount );
        }

//        @Override
//        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
//            if(power instanceof OldWebPower && target == owner) {
//                flash();
//                power.amount += 2;
//            }
//        }

        @Override
        public void atStartOfTurnPostDraw() {
            flash();
            addToBot(new ApplyPowerAction(owner, owner, new WebPower(owner, amount), amount ) );
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        }

        @Override
        public AbstractPower makeCopy() {
            return new LairPower(owner, amount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_WEB);
            initializeDescription();
        }
    }

//    @Override
//    public void downgrade() {
//        if (upgraded) {
//            name = cardStrings.NAME;
//            timesUpgraded--;
//            upgraded = false;
//            magicNumber = baseMagicNumber = WEB;
//            upgradedMagicNumber = false;
//        }
//    }
}
