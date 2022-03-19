package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
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
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class Scrapper extends CustomCard {
    public static final String ID = WidowMod.makeID(Scrapper.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Scrapper() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Scrapper.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.POWER,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.SELF );
        magicNumber = baseMagicNumber = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.apply(new ScrapperPower(p, magicNumber));
    }

    public static class ScrapperPower extends AbstractEasyPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(ScrapperPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        public ScrapperPower(final AbstractCreature owner, final int amount) {
            super( powerStrings.NAME,
                    PowerType.BUFF,
                    owner,
                    amount );
        }

        @Override
        public void updateDescription() {
            description = String.format(powerStrings.DESCRIPTIONS[0], amount);
        }

        @Override
        public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
            int count = Wiz.count(owner.powers, p -> p.type == PowerType.DEBUFF);
            if (count > 0) {
                flash();
                addToBot(new GainBlockAction(owner, count * amount));
            }
        }

        @Override
        public AbstractPower makeCopy() {
            return new ScrapperPower(owner, amount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(2);
            initializeDescription();
        }
    }
}
