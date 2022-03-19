package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.AbstractEasyPower;
import theWidow.util.Wiz;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;

public class Vengeful extends CustomCard {
    public static final String ID = WidowMod.makeID(Vengeful.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Vengeful() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Vengeful.class.getSimpleName()),
                0,
                cardStrings.DESCRIPTION,
                CardType.POWER,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.SELF );
        magicNumber = baseMagicNumber = 5;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.apply(new VengefulPower(p, magicNumber));
    }

    public static class VengefulPower extends AbstractEasyPower implements CloneablePowerInterface, OnReceivePowerPower {
        public static final String POWER_ID = WidowMod.makeID(VengefulPower.class.getSimpleName());
        private static final PowerStrings powerStrings = languagePack.getPowerStrings(POWER_ID);

        public VengefulPower(final AbstractCreature owner, final int amount) {
            super( powerStrings.NAME,
                    PowerType.BUFF,
                    owner,
                    amount );
        }

        @Override
        public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
            if (power.type == PowerType.DEBUFF && !target.hasPower(ArtifactPower.POWER_ID)) {
                flash();
                addToTop(new DamageRandomEnemyAction(new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            }
            return true;
        }

        @Override
        public void updateDescription() {
            description = String.format(powerStrings.DESCRIPTIONS[0], amount);
        }

        @Override
        public AbstractPower makeCopy() {
            return new VengefulPower(owner, amount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(3);
        }
    }
}
