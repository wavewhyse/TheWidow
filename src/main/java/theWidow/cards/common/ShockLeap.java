package theWidow.cards.common;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.blue.Zap;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.CardArtRoller;
import theWidow.util.Wiz;
import theWidow.vfx.ShockEffect;

import static theWidow.WidowMod.makeCardPath;

public class ShockLeap extends CustomCard {
    public static final String ID = WidowMod.makeID(ShockLeap.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public ShockLeap() {
        super( ID,
                cardStrings.NAME,
                makeCardPath("Skill"),
                0,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.COMMON,
                CardTarget.SELF );
        magicNumber = baseMagicNumber = 3;
        exhaust = true;
        if (CardLibrary.getAllCards() != null && !CardLibrary.getAllCards().isEmpty())
            CardArtRoller.computeCard(this, Zap.ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
        addToBot(new VFXAction(new ShockEffect(p.hb.cX, p.hb.cY)));
        addToBot(new VFXAction(new ShockEffect(p.hb.cX, p.hb.cY)));
        addToBot(new VFXAction(new ShockEffect(p.hb.cX, p.hb.cY)));

        Wiz.apply(new StrengthPower(p, magicNumber));
        Wiz.apply(new LoseStrengthPower(p, magicNumber));
        Wiz.apply(new DexterityPower(p, magicNumber));
        Wiz.apply(new LoseDexterityPower(p, magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(2);
        }
    }
}
