package theWidow.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.SearingBlowEffect;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.cards.BetaCard;

import static theWidow.WidowMod.makeCardPath;

public class SearingBlade extends BetaCard {
    public static final String ID = WidowMod.makeID(SearingBlade.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public SearingBlade() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(SearingBlade.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.COMMON,
                CardTarget.ENEMY,
                cardStrings );
        baseDamage = 9;
        SewingKitCheck();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (timesUpgraded > 5)
            addToBot(new VFXAction(new SearingBlowEffect(m.hb.cX, m.hb.cY, timesUpgraded)));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public void upgrade() {
        upgradeDamage(2 +timesUpgraded);
        upgradeName();
    }

    @Override
    public void downgrade() {
        super.downgrade();
        baseDamage -= ( 2 + timesUpgraded );
    }
}