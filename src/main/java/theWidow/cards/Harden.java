package theWidow.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.relics.SewingKitRelic;

import static theWidow.WidowMod.makeCardPath;

public class Harden extends BetaCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Harden.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Harden.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;

    private static final int BLOCK = 8;
    private static final int UPGRADE_PLUS_BLOCK = 1;

    // /STAT DECLARATION/

    public Harden() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SewingKitRelic.ID))
            upgrade();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new GainBlockAction(p, block));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        upgradeBlock(UPGRADE_PLUS_BLOCK + timesUpgraded);
        upgradeName();
    }

    @Override
    public void downgrade() {
        super.downgrade();
        baseBlock -= ( UPGRADE_PLUS_BLOCK + timesUpgraded );
    }
}