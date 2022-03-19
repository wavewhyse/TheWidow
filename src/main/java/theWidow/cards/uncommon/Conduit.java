package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
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

public class Conduit extends CustomCard {

    public static final String ID = WidowMod.makeID(Conduit.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Conduit() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Conduit.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.SELF );
        baseMagicNumber = magicNumber = 3;
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.apply(new WebPower(p, magicNumber));
        damage = magicNumber;
        if (p.hasPower(WebPower.POWER_ID))
            damage += p.getPower(WebPower.POWER_ID).amount;
        baseDamage = damage;
        calculateCardDamage(m);
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    @Deprecated
    public static class ConduitPower extends AbstractEasyPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(ConduitPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        public ConduitPower(AbstractCreature owner, int amount) {
            super( powerStrings.NAME,
                    PowerType.BUFF,
                    owner,
                    amount );
        }

        @Override
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0];
        }

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type) {
            if (type == DamageInfo.DamageType.NORMAL && owner.hasPower(WebPower.POWER_ID))
                return damage + owner.getPower(WebPower.POWER_ID).amount;
            else return damage;
        }

        @Override
        public void onAfterUseCard(AbstractCard card, UseCardAction action) {
            if (card.type == CardType.ATTACK)
                addToBot(new ReducePowerAction(owner, owner, this, 1));
        }

        @Override
        public AbstractPower makeCopy() {
            return new ConduitPower(owner, amount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(2);
        }
    }
}
