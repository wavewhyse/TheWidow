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
import theWidow.powers.AbstractEasyPower;
import theWidow.powers.WebPower;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class HermitWeave extends CustomCard {
    public static final String ID = WidowMod.makeID(HermitWeave.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public HermitWeave() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(HermitWeave.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.POWER,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.SELF );
        magicNumber = baseMagicNumber = 3;
    }
    
    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        Wiz.apply(new HermitWeavePower(p, magicNumber));
    }

    public static class HermitWeavePower extends AbstractEasyPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(HermitWeavePower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        public HermitWeavePower(final AbstractCreature owner, final int amount) {
            super( powerStrings.NAME,
                    PowerType.BUFF,
                    owner,
                    amount );
        }

        @Override
        public void atEndOfTurn(boolean isPlayer) {
            if (isPlayer)
                Wiz.apply(new WebPower(owner, amount));
        }

        @Override
        public void updateDescription() {
            description = String.format(powerStrings.DESCRIPTIONS[0], amount);
        }

        @Override
        public AbstractPower makeCopy() {
            return new HermitWeavePower(owner, amount);
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