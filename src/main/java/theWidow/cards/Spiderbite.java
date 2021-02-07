package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import theWidow.WidowMod;
import theWidow.characters.TheWidow;
import theWidow.powers.NecrosisPower;

import static theWidow.WidowMod.makeCardPath;

public class Spiderbite extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Spiderbite.class.getSimpleName());
    public static final String IMG = makeCardPath("Spiderbite.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int NECROSIS = 3;
    private static final int UPGRADE_PLUS_NECROSIS = 1;

    // /STAT DECLARATION/

    public Spiderbite() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = NECROSIS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new VFXAction(new BiteEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale, Settings.GOLD_COLOR.cpy()), 0.3F));
        addToBot( new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        addToBot( new ApplyPowerAction(m, p, new NecrosisPower(m, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_NECROSIS);
            initializeDescription();
        }
    }
}
