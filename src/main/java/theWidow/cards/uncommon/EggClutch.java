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
import theWidow.patches.EggClutchPatch;
import theWidow.powers.AbstractEasyPower;
import theWidow.util.Wiz;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;

public class EggClutch extends CustomCard {
    public static final String ID = WidowMod.makeID(EggClutch.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public EggClutch() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(EggClutch.class.getSimpleName()),
                2,
                cardStrings.DESCRIPTION,
                CardType.POWER,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.SELF );
        magicNumber = baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.apply(new EggClutchPower(p, magicNumber));
    }

    public static class EggClutchPower extends AbstractEasyPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(EggClutchPower.class.getSimpleName());
        private static final PowerStrings powerStrings = languagePack.getPowerStrings(POWER_ID);

        public EggClutchPower(AbstractCreature owner, int amount) {
            super( powerStrings.NAME,
                    PowerType.BUFF,
                    owner,
                    amount );
        }

        @Override
        public void updateDescription() {
            if (amount == 1)
                description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1] + powerStrings.DESCRIPTIONS[3];
            else
                description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[2] + powerStrings.DESCRIPTIONS[3];
        }

        @Override
        public void onVictory() {
            flash();
            EggClutchPatch.EGG_CLUTCH_UPGRADES = amount;
        }

        @Override
        public AbstractPower makeCopy() {
            return new EggClutchPower(owner, amount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(1);
        }
    }
}
