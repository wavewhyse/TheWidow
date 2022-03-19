package theWidow.cards.rare;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.helpers.BaseModCardTags;
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
import theWidow.actions.TransformCursesAction;
import theWidow.actions.WidowUpgradeManagerAction;
import theWidow.powers.AbstractEasyPower;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class TranscendentForm extends CustomCard {
    public static final String ID = WidowMod.makeID(TranscendentForm.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public TranscendentForm() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(TranscendentForm.class.getSimpleName()),
                3,
                cardStrings.DESCRIPTION,
                CardType.POWER,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.RARE,
                CardTarget.SELF );
        magicNumber = baseMagicNumber = 1;
        tags.add(BaseModCardTags.FORM);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.apply(new TranscensionPower(p, magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            initializeDescription();
        }
    }

    public static class TranscensionPower extends AbstractEasyPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(TranscensionPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        public TranscensionPower(final AbstractCreature owner, final int amount) {
            super( powerStrings.NAME,
                    PowerType.BUFF,
                    owner,
                    amount );
        }

        @Override
        public void updateDescription() {
            if(amount == 1)
                description = powerStrings.DESCRIPTIONS[0] + powerStrings.DESCRIPTIONS[3];
            else
                description = powerStrings.DESCRIPTIONS[0] + powerStrings.DESCRIPTIONS[1] + amount + powerStrings.DESCRIPTIONS[3];
        }

        @Override
        public void atStartOfTurnPostDraw() {
            flash();
            addToBot(new TransformCursesAction());
            for (int i=0; i<amount; i++)
                addToBot(new WidowUpgradeManagerAction(BaseMod.MAX_HAND_SIZE));
        }

        @Override
        public AbstractPower makeCopy() {
            return new TranscensionPower(owner, amount);
        }

    }
}
