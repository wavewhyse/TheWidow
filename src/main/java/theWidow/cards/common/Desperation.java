package theWidow.cards.common;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class Desperation extends CustomCard {
    public static final String ID = WidowMod.makeID(Desperation.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Desperation() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Desperation.class.getSimpleName()),
                0,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.COMMON,
                CardTarget.ALL_ENEMY );
        baseDamage = 10;
        magicNumber = baseMagicNumber = 2;
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        Wiz.apply(new WeakPower(p, magicNumber, false));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(3);
            initializeDescription();
        }
    }
}
