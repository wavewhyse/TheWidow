package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.powers.AbstractEasyPower;
import theWidow.util.Wiz;
import theWidow.vfx.ShockEffect;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class Tazer extends CustomCard {
    public static final String ID = WidowMod.makeID(Tazer.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Tazer() {
        super( ID,
                cardStrings.NAME,
                WidowMod.makeCardPath(Tazer.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                theWidow.TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.ENEMY );
        baseDamage = 7;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        Wiz.apply(new TazerPower(m, 3));
    }

    public static class TazerPower extends AbstractEasyPower implements CloneablePowerInterface, NonStackablePower {
        public static final String POWER_ID = WidowMod.makeID(TazerPower.class.getSimpleName());
        private static final PowerStrings powerStrings = languagePack.getPowerStrings(POWER_ID);

        public TazerPower(final AbstractMonster owner, final int amount) {
            super( powerStrings.NAME,
                    PowerType.DEBUFF,
                    owner,
                    amount
            );
        }

        @Override
        public void atEndOfRound() {
            if (amount <= 1) {
                flash();
                addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
                addToBot(new VFXAction(new ShockEffect(owner.hb.cX, owner.hb.cY)));
                addToBot(new StunMonsterAction((AbstractMonster) owner, Wiz.adp()));
                addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            } else {
                flashWithoutSound();
                addToBot(new ReducePowerAction(owner, owner, this, 1));
            }
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
            return new TazerPower((AbstractMonster) owner, amount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(4);
        }
    }
}
