package theWidow.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
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

public class SearingBlade extends BetaCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(SearingBlade.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("SearingBlade.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;

    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 2;

    // /STAT DECLARATION/

    public SearingBlade() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SewingKitRelic.ID))
            upgrade();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        upgradeDamage(UPGRADE_PLUS_DMG+timesUpgraded);
        upgradeName();
    }

    @Override
    public AbstractCard makeCopy() {
        return new SearingBlade();
    }

    @Override
    public void downgrade() {
        super.downgrade();
        baseDamage -= ( UPGRADE_PLUS_DMG + timesUpgraded );
    }
}