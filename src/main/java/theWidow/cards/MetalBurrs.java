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
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import theWidow.TheWidow;
import theWidow.WidowMod;

import static theWidow.WidowMod.makeCardPath;

public class MetalBurrs extends CustomCard {

    public static final String ID = WidowMod.makeID(MetalBurrs.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("MetalBurrs.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 3;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DMG = 1;
    private static final int HITS = 4;

    private int discount;

    public MetalBurrs() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
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
            setCostForTurn(costForTurn + discount - debuffCount);
            discount = debuffCount;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            if (Settings.FAST_MODE) {
                addToBot(new VFXAction( new ViolentAttackEffect(m.hb.cX, m.hb.cY, Color.BLACK), 0.1f));
            } else {
                addToBot(new VFXAction( new ViolentAttackEffect(m.hb.cX, m.hb.cY, Color.BLACK), 0.4F));
            }
        }
        for (int i=0; i<HITS; i++)
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }

//    @Override
//    public AbstractCard makeCopy() {
//        return new MetalBurrs();
//    }
}
