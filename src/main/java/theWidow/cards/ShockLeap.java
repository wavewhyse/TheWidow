package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.vfx.ShockEffect;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;

public class ShockLeap extends CustomCard {

    public static final String ID = WidowMod.makeID(ShockLeap.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");// "public static final String IMG = makeCardPath("ShockLeap.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 0;
    private static final int STATS = 3;
    private static final int UPGRADE_PLUS_STATS = 2;

    public ShockLeap() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = STATS;
        exhaust = true;
        if (CardLibrary.getAllCards() != null && !CardLibrary.getAllCards().isEmpty())
            theWidow.util.artHelp.CardArtRoller.computeCard(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
        addToBot(new VFXAction(new ShockEffect(p.hb.cX, p.hb.cY)));
        addToBot(new VFXAction(new ShockEffect(p.hb.cX, p.hb.cY)));
        addToBot(new VFXAction(new ShockEffect(p.hb.cX, p.hb.cY)));

        addToBot( new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber));
        addToBot( new ApplyPowerAction(p, p, new LoseStrengthPower(p, magicNumber), magicNumber));
        addToBot( new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber), magicNumber));
        addToBot( new ApplyPowerAction(p, p, new LoseDexterityPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_STATS);
        }
    }
}
