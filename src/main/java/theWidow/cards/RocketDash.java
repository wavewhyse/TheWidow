package theWidow.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.relics.SewingKitRelic;

import static theWidow.WidowMod.makeCardPath;

public class RocketDash extends BetaCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(RocketDash.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("RocketDash.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int MAGIC = 6;
    private static final int BLOCK = 14;
    private static final int UPGRADE_PLUS_BLOCK = 1;

    private boolean damageQueued;

    // /STAT DECLARATION/

    public RocketDash() {
        this(0);
    }

    public RocketDash(int upgrades) {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        baseBlock = BLOCK;
        timesUpgraded = upgrades;
        damageQueued = false;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SewingKitRelic.ID))
            upgrade();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new
                GainBlockAction(p, block)
        );
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (damageQueued && AbstractDungeon.player.hand.contains(this)) {
            superFlash();
            addToBot(new DamageAllEnemiesAction(
                    null,
                    DamageInfo.createDamageMatrix(magicNumber, true),
                    DamageInfo.DamageType.THORNS,
                    AbstractGameAction.AttackEffect.FIRE));
            damageQueued = false;
        }
    }

    @Override
    public void triggerWhenDrawn() {
        damageQueued = false;
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        upgradeBlock(UPGRADE_PLUS_BLOCK );
        upgradeName();
        damageQueued = true;
    }

    @Override
    public AbstractCard makeCopy() {
        return new RocketDash();
    }

    @Override
    public void downgrade() {
        super.downgrade();
        baseBlock -= UPGRADE_PLUS_BLOCK + timesUpgraded;
    }
}
