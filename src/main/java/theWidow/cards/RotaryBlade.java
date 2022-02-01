package theWidow.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeCardAction;
import theWidow.relics.SewingKitRelic;

import static theWidow.WidowMod.makeCardPath;

public class RotaryBlade extends BetaCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(RotaryBlade.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("RotaryBlade.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;

    private static final int DAMAGE = 4;
    private static final int HITS = 3;
    private static final int UPGRADE_PLUS_HITS = 1;

    // /STAT DECLARATION/

    public RotaryBlade() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = HITS;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SewingKitRelic.ID))
            upgrade();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++)
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new WidowUpgradeCardAction(this));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        upgradeMagicNumber(UPGRADE_PLUS_HITS);
        upgradeName();
    }

    @Override
    public void downgrade() {
        magicNumber = baseMagicNumber = baseMagicNumber - UPGRADE_PLUS_HITS;
        super.downgrade();
    }
}