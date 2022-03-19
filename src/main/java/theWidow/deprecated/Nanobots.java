package theWidow.deprecated;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.cards.BetaCard;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
public class Nanobots extends BetaCard {
    public static final String ID = WidowMod.makeID(Nanobots.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DMG = 2;

    private boolean shuffleQueued;



    public Nanobots() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Nanobots.class.getSimpleName()),
                0,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.COMMON,
                CardTarget.ENEMY,
                cardStrings );
        baseDamage = DAMAGE;
        shuffleQueued = false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (shuffleQueued && Wiz.adp().hand.contains(this)) {
            addToBot(new MakeTempCardInDiscardAction(this.makeStatEquivalentCopy(), 1));
            shuffleQueued = false;
        }
    }

    @Override
    public void triggerWhenDrawn() {
        shuffleQueued = false;
    }

    @Override
    public void upgrade() {
        upgradeName();
        upgradeDamage(UPGRADE_PLUS_DMG);
        shuffleQueued = true;
    }
}
