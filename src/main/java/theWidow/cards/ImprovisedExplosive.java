package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.potions.PulseBombPotion;

import static theWidow.WidowMod.makeCardPath;

public class ImprovisedExplosive extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(ImprovisedExplosive.class.getSimpleName());
    public static final String IMG = makeCardPath("ImprovisedExplosive.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int SELF_DAMAGE = 6;
    private static final int UPGRADE_PLUS_SELF_DAMAGE = -3;

    // /STAT DECLARATION/

    public ImprovisedExplosive() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = SELF_DAMAGE;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(p, new DamageInfo(p, magicNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        addToBot(new ObtainPotionAction(new PulseBombPotion()));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_SELF_DAMAGE);
            initializeDescription();
        }
    }
}
