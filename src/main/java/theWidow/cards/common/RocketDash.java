package theWidow.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
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

public class RocketDash extends BetaCard {
    public static final String ID = WidowMod.makeID(RocketDash.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private boolean damageQueued;

    public RocketDash() {
        this(0);
    }

    public RocketDash(int upgrades) {
        super( ID,
                cardStrings.NAME,
                makeCardPath(RocketDash.class.getSimpleName()),
                2,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.COMMON,
                CardTarget.SELF,
                cardStrings );
        magicNumber = baseMagicNumber = 8;
        baseBlock = 14;
        timesUpgraded = upgrades;
        damageQueued = false;
        SewingKitCheck();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (damageQueued && Wiz.adp().hand.contains(this)) {
            superFlash();
            addToBot(new DamageAllEnemiesAction(
                    null,
                    DamageInfo.createDamageMatrix(magicNumber, true),
                    DamageInfo.DamageType.THORNS,
                    AbstractGameAction.AttackEffect.FIRE));
            damageQueued = false;
        }
    }

    @Override
    public void triggerWhenDrawn() {
        damageQueued = false;
    }

    @Override
    public void upgrade() {
        upgradeBlock(2);
        upgradeName();
        damageQueued = true;
    }

    @Override
    public void downgrade() {
        super.downgrade();
        baseBlock -= 2 + timesUpgraded;
    }
}
