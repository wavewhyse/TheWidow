package theWidow.cards.rare;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.AbstractEasyPower;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class BombLauncher extends CustomCard {
    public static final String ID = WidowMod.makeID(BombLauncher.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public BombLauncher() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(BombLauncher.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.RARE,
                CardTarget.SELF );
        magicNumber = baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.apply(new BombLauncherPower(p, magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            initializeDescription();
        }
    }

    public static class BombLauncherPower extends AbstractEasyPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(BombLauncherPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        public BombLauncherPower(final AbstractCreature owner, final int amount) {
            super( powerStrings.NAME,
                    PowerType.BUFF,
                    owner,
                    amount );
        }

        @Override
        public void updateDescription() {
            if (amount == 1)
                description = powerStrings.DESCRIPTIONS[0];
            else
                description = String.format(powerStrings.DESCRIPTIONS[1], amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }

        public void onPotionUse(AbstractPotion pot, AbstractCreature target) {
            flash();
            pot.use(target);
            if (amount <= 1)
                addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            else
                addToBot(new ReducePowerAction(owner, owner, this, 1));
        }

        @Override
        public AbstractPower makeCopy() {
            return new BombLauncherPower(owner, amount);
        }
    }
}
