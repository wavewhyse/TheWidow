package theWidow.cards.rare;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.OddMushroom;
import com.megacrit.cardcrawl.relics.PaperCrane;
import com.megacrit.cardcrawl.relics.PaperFrog;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.AbstractEasyPower;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class Unstoppable extends CustomCard {
    public static final String ID = WidowMod.makeID(Unstoppable.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Unstoppable() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Unstoppable.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.RARE,
                CardTarget.ENEMY );
        damage = baseDamage = 10;
        magicNumber = baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new InflameEffect(p)));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        Wiz.apply(new UnstoppablePower(p, magicNumber));
//        addToBot(new RemoveSpecificPowerAction(p, p, VulnerablePower.POWER_ID));
//        addToBot(new RemoveSpecificPowerAction(p, p, WeakPower.POWER_ID));
//        addToBot(new RemoveSpecificPowerAction(p, p, FrailPower.POWER_ID));
    }

    public static class UnstoppablePower extends AbstractEasyPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(UnstoppablePower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        public UnstoppablePower(AbstractCreature owner, int amount) {
            super( powerStrings.NAME,
                    PowerType.BUFF,
                    owner,
                    amount );
        }

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type) {
            if (owner.hasPower(WeakPower.POWER_ID) && type == DamageInfo.DamageType.NORMAL)
                return !owner.isPlayer && Wiz.adp().hasRelic(PaperCrane.ID) ? damage / 0.6F : damage / 0.75F;
            else return damage;
        }

        @Override
        public float atDamageReceive(float damage, DamageInfo.DamageType type) {
            if (owner.hasPower(VulnerablePower.POWER_ID) && type == DamageInfo.DamageType.NORMAL) {
                if (owner.isPlayer && Wiz.adp().hasRelic(OddMushroom.ID))
                    return damage / 1.25F;
                else
                    return !owner.isPlayer && Wiz.adp().hasRelic(PaperFrog.ID) ? damage / 1.75F : damage / 1.5F;
            } else return damage;
        }

        public float modifyBlock(float blockAmount) {
            if (owner.hasPower(FrailPower.POWER_ID))
                return blockAmount / 0.75F;
            else return blockAmount;
        }

        @Override
        public void atEndOfRound() {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }

        @Override
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0];
        }

        @Override
        public AbstractPower makeCopy() {
            return new UnstoppablePower(owner, amount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(4);
            upgradeMagicNumber(1);
            initializeDescription();
        }
    }
}
