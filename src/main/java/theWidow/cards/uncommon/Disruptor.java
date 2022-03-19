package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.AbstractEasyPower;
import theWidow.util.Wiz;
import theWidow.vfx.ShockEffect;

import static theWidow.WidowMod.makeCardPath;

public class Disruptor extends CustomCard {

    public static final String ID = WidowMod.makeID(Disruptor.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Disruptor() {
        super( ID,
                cardStrings.NAME,
                makeCardPath("ShockEmitters"),
                1,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.ENEMY );
        baseDamage = 7;
        magicNumber = baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
        addToBot(new VFXAction(new ShockEffect(m.hb.cX, m.hb.cY)));
        addToBot(new VFXAction(new ShockEffect(m.hb.cX, m.hb.cY)));
        addToBot(new VFXAction(new ShockEffect(m.hb.cX, m.hb.cY)));
        addToBot(new VFXAction(new ShockEffect(m.hb.cX, m.hb.cY)));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn)));
        Wiz.apply(new DisruptorPower(m, magicNumber));
    }

    public static class DisruptorPower extends AbstractEasyPower implements CloneablePowerInterface, OnReceivePowerPower {
        public static final String POWER_ID = WidowMod.makeID(DisruptorPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        public DisruptorPower(final AbstractCreature owner, final int amount) {
            super( powerStrings.NAME,
                    PowerType.DEBUFF,
                    owner,
                    amount );
        }

        @Override
        public boolean onReceivePower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
            return !(p instanceof StrengthPower && p.amount > 0);
        }

        @Override
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0];
        }

        @Override
        public void atEndOfRound() {
            addToBot(new ReducePowerAction(owner, Wiz.adp(), this, 1));
        }

        @Override
        public AbstractPower makeCopy() {
            return new DisruptorPower(owner, amount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(2);
            upgradeMagicNumber(1);
            initializeDescription();
        }
    }
}
