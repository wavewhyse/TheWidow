package theWidow.cards.common;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.potions.GrenadePotion;
import theWidow.powers.GrenadierPower;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class ExtraCompartment extends CustomCard {
    public static final String ID = WidowMod.makeID(ExtraCompartment.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public ExtraCompartment() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(ExtraCompartment.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.COMMON,
                CardTarget.NONE );
        magicNumber = baseMagicNumber = 1;
        ExhaustiveVariable.setBaseValue(this, 2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.apply(new ExtraCompartmentPower(p, 1));
        addToBot(new ObtainPotionAction(new GrenadePotion()));
    }

    public static class ExtraCompartmentPower extends GrenadierPower {
        public static final String POWER_ID = WidowMod.makeID(ExtraCompartmentPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        public ExtraCompartmentPower(final AbstractCreature owner, final int amount) {
            super( powerStrings.NAME,
                    owner,
                    amount );
        }

        @Override
        public void atEndOfTurn(boolean isPlayer) {
            if (isPlayer)
                addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }

        @Override
        public AbstractPower makeCopy() {
            return new ExtraCompartmentPower(owner, amount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            ExhaustiveVariable.upgrade(this, 1);
        }
    }
}
