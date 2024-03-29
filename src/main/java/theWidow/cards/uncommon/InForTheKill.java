package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import theWidow.TheWidow;
import theWidow.WidowMod;

import static theWidow.WidowMod.makeCardPath;

public class InForTheKill extends CustomCard {
    public static final String ID = WidowMod.makeID(InForTheKill.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public InForTheKill() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(InForTheKill.class.getSimpleName()),
                2,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.ENEMY );
        baseDamage = 14;
        magicNumber = baseMagicNumber = 2;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.hasPower(VulnerablePower.POWER_ID)) {
            addToBot(new VFXAction(new VfxBuilder(ImageMaster.DAGGER_STREAK, (m.hb.cX + p.drawX) / 2f, m.hb.cY, 0.4f)
                    .setColor(Color.RED.cpy())
                    .fadeIn(0.2f)
                    .andThen(0.4f)
                    .moveX((m.hb.cX + p.drawX) / 2f, m.hb.x + m.hb.width, VfxBuilder.Interpolations.SWINGIN)
                    .fadeOut(0.3f)
                    .playSoundAt(0.2f, "IN_FOR_THE_KILL")
                    .build()
            , 0.4f));
            addToBot(new WaitAction(0.6f));
        }
        else
            addToBot(new VFXAction(new ThrowDaggerEffect(m.hb.cX, m.hb.cY)));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo); //TODO: Technically there are rounding errors with this method, a patch would be required to fix
        if(mo.hasPower(VulnerablePower.POWER_ID)) {
            float tmp = damage;
            for (int i = 0; i < magicNumber - 1; i++)
                tmp = mo.getPower(VulnerablePower.POWER_ID).atDamageReceive(tmp, damageTypeForTurn, this);
            damage = MathUtils.floor(tmp);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            initializeDescription();
        }
    }
}
