package theWidow.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.WidowMod;
import theWidow.characters.TheWidow;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;

public class UnstableCell extends BetaCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(UnstableCell.class.getSimpleName());
    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("UnstableCell.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 1;

    private boolean playQueued;

    // /STAT DECLARATION/

    public UnstableCell() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        playQueued = false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot( new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public void triggerWhenDrawn() {
        playQueued = false;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (playQueued && AbstractDungeon.player.hand.contains(this)) {
            superFlash();
            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true, energyOnUse, true, true));
            playQueued = false;
        }
    }

    @Override
    public void upgrade() {
        upgradeName();
        upgradeDamage(UPGRADE_PLUS_DMG + timesUpgraded);
        playQueued = true;
    }

    @Override
    public void downgrade() {
        super.downgrade();
        baseDamage -= (UPGRADE_PLUS_DMG + timesUpgraded);
    }
}
