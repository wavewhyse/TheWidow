package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import theWidow.TheWidow;
import theWidow.WidowMod;

import static theWidow.WidowMod.makeCardPath;

public class Desperation extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Desperation.class.getSimpleName());
    public static final String IMG = makeCardPath("Desperation.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 0;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int WEAK = 2;

    // /STAT DECLARATION/

    public Desperation() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = WEAK;
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        addToBot(new ApplyPowerAction(p, p, new WeakPower(p, magicNumber, false), magicNumber));
        //for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters)
        //    addToBot(new ApplyPowerAction(mon, p, new ParalysisPower(mon, 1), 1));
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
