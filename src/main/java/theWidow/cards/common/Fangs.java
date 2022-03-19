package theWidow.cards.common;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import theWidow.WidowMod;

public class Fangs extends CustomCard {
    public static final String ID = WidowMod.makeID(Fangs.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Fangs() {
        super( ID,
                cardStrings.NAME,
                WidowMod.makeCardPath("Spiderbite"),
                1,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                theWidow.TheWidow.Enums.COLOR_BLACK,
                CardRarity.COMMON,
                CardTarget.ENEMY );
        baseDamage = 8;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot(new VFXAction(new BiteEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale, Color.RED.cpy()), Settings.FAST_MODE ? 0.1f : 0.3f));
        }
        DamageInfo dmg = new DamageInfo(p, damage, damageTypeForTurn);
        dmg.name = ID;
        addToBot(new DamageAction(m, dmg));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(4);
        }
    }
}
