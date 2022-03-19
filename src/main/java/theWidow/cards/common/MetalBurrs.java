package theWidow.cards.common;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class MetalBurrs extends CustomCard {
    public static final String ID = WidowMod.makeID(MetalBurrs.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private int discount;

    public MetalBurrs() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(MetalBurrs.class.getSimpleName()),
                3,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.COMMON,
                CardTarget.ENEMY );
        baseDamage = 4;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        calculateDiscount();
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
        discount = 0;
        calculateDiscount();
    }

    private void calculateDiscount() {
        int debuffCount = Wiz.count(Wiz.adp().powers, pow -> pow.type == AbstractPower.PowerType.DEBUFF);
        if (discount != debuffCount) {
            setCostForTurn(costForTurn + discount - debuffCount);
            discount = debuffCount;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new ViolentAttackEffect(m.hb.cX, m.hb.cY, Color.BLACK), Settings.FAST_MODE ? 0.1f : 0.4f));
        for (int i = 0; i< 4; i++)
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(1);
        }
    }
}
