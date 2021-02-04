package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import theWidow.WidowMod;
import theWidow.characters.TheWidow;

import static theWidow.WidowMod.makeCardPath;

public class MetalBurrs extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(MetalBurrs.class.getSimpleName());
    public static final String IMG = makeCardPath("MetalBurrs.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 3;
    private static final int DAMAGE = 16;
    private static final int UPGRADE_PLUS_DMG = 4;

    private int discount;

    // /STAT DECLARATION/

    public MetalBurrs() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
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
        int debuffCount = (int) AbstractDungeon.player.powers.stream().filter(pow -> pow.type == AbstractPower.PowerType.DEBUFF).count();
        if (discount != debuffCount) {
            costForTurn += discount - debuffCount;
            if (costForTurn < 0)
                costForTurn = 0;
            discount = debuffCount;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            if (Settings.FAST_MODE) {
                addToBot(new VFXAction( new ViolentAttackEffect(m.hb.cX, m.hb.cY, Color.BLACK)));
            } else {
                addToBot(new VFXAction( new ViolentAttackEffect(m.hb.cX, m.hb.cY, Color.BLACK), 0.4F));
            }
        }
        addToBot( new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
