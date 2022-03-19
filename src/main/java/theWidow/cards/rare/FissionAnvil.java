package theWidow.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeCardAction;
import theWidow.cards.BetaCard;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class FissionAnvil extends BetaCard {
    public static final String ID = WidowMod.makeID(FissionAnvil.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private boolean actionQueued;

    public FissionAnvil() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(FissionAnvil.class.getSimpleName()),
                2,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.RARE,
                CardTarget.ENEMY,
                cardStrings );
        baseDamage = 20;
        SewingKitCheck();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null)
            addToBot(new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY), Settings.ACTION_DUR_MED));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (actionQueued
                && Wiz.isInCombat()
                && Wiz.adp().hand.contains(this)) {
            superFlash();
            for (AbstractCard c : Wiz.adp().hand.group) {
                if (!(c instanceof FissionAnvil) && c.canUpgrade())
                    addToTop(new WidowUpgradeCardAction(c));
            }
            actionQueued = false;
        }
    }

    @Override
    public void upgrade() {
        upgradeDamage(4 + timesUpgraded);
        upgradeName();
        actionQueued = true;
    }

    @Override
    public void downgrade() {
        super.downgrade();
        baseDamage -= 4 + timesUpgraded;
    }
}
