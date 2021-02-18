package theWidow.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;

public class Nanobots extends BetaCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Nanobots.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Nanobots.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 0;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DMG = 2;

    // /STAT DECLARATION/

    private boolean shuffleQueued;

    public Nanobots() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        shuffleQueued = false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot( new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (shuffleQueued && AbstractDungeon.player.hand.contains(this)) {
            addToBot(new MakeTempCardInDiscardAction(this.makeStatEquivalentCopy(), 1));
            shuffleQueued = false;
        }
    }

    @Override
    public void triggerWhenDrawn() {
        shuffleQueued = false;
    }

    @Override
    public void upgrade() {
        upgradeName();
        upgradeDamage(UPGRADE_PLUS_DMG);
        shuffleQueued = true;
    }
}
