package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import theWidow.WidowMod;
import theWidow.characters.TheWidow;
import theWidow.powers.ParalysisPower;

import static theWidow.WidowMod.makeCardPath;

public class Stinger extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Stinger.class.getSimpleName());
    public static final String IMG = makeCardPath("Stinger.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int DAMAGE = 14;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int PARALYSIS = 2;
    private static final int UPGRADE_PLUS_PARALYSIS = 1;

    // /STAT DECLARATION/

    public Stinger() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = PARALYSIS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null)
            addToBot(new VFXAction(new ThrowDaggerEffect(m.hb.cX, m.hb.cY)));
        addToBot( new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn)));
        addToBot( new ApplyPowerAction(m, p, new ParalysisPower(m, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_PARALYSIS);
            initializeDescription();
        }
    }
}
