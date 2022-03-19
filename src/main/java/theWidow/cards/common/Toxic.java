package theWidow.cards.common;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.SapPower;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class Toxic extends CustomCard {
    public static final String ID = WidowMod.makeID(Toxic.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Toxic() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Toxic.class.getSimpleName()),
                0,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.COMMON,
                CardTarget.ENEMY );
        magicNumber = baseMagicNumber = 4;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new PotionBounceEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), Settings.ACTION_DUR_MED));
        Wiz.apply(new SapPower(m, magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(2);
        }
    }
}
