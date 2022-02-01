package theWidow.deprecated;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.CaughtPower;
import theWidow.powers.ParalysisPower;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
@Deprecated
public class Opportunist extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Opportunist.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Attack.png");// "public static final String IMG = makeCardPath("Opportunist.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 3;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int VULN = 2;

    // /STAT DECLARATION/

    public Opportunist() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = VULN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                boolean attacking = false;
                switch (m.intent) {
                    case ATTACK:
                    case ATTACK_BUFF:
                    case ATTACK_DEBUFF:
                    case ATTACK_DEFEND:
                        break;
                    default:
                        attacking = true;
                        break;
                }
                for (AbstractPower pow : m.powers)
                    if (pow instanceof WeakPower || pow instanceof ParalysisPower || pow instanceof CaughtPower) {
                        attacking = true;
                        break;
                    }
                if (attacking) {
                    Opportunist.this.calculateCardDamage(m);
                    DamageInfo info = new DamageInfo(p, damage, damageTypeForTurn);
                    addToTop(new DamageAction(m, info, AttackEffect.SLASH_HEAVY));
                    addToTop(new DamageAction(m, info, AttackEffect.SLASH_HORIZONTAL));
                }
                isDone = true;
            }
        });
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
