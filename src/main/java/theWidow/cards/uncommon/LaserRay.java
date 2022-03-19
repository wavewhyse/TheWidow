package theWidow.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.cards.BetaCard;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class LaserRay extends BetaCard {
    public static final String ID = WidowMod.makeID(LaserRay.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public LaserRay() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(LaserRay.class.getSimpleName()),
                2,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.ENEMY,
                cardStrings );
        baseDamage = 13;
        magicNumber = baseMagicNumber = 1;
        SewingKitCheck();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        Wiz.apply(new WeakPower(m, magicNumber, false));
        Wiz.apply(new VulnerablePower(m, magicNumber, false));
    }

    @Override
    public void upgrade() {
        upgradeMagicNumber(1);
        upgradeName();
        initializeDescription();
    }

    @Override
    public void downgrade() {
        magicNumber = baseMagicNumber = baseMagicNumber - 1;
        super.downgrade();
    }

    @Override
    public AbstractCard makeCopy() {
        return new LaserRay();
    }
}
