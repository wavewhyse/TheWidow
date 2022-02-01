package theWidow.deprecated;

import basemod.AutoAdd;
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
import theWidow.cards.BetaCard;
import theWidow.relics.SewingKitRelic;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
@Deprecated
public class FiberglassDagger extends BetaCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(FiberglassDagger.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Attack.png");// "public static final String IMG = makeCardPath("FiberglassDagger.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 3;

    // /STAT DECLARATION/

    public FiberglassDagger() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SewingKitRelic.ID))
            upgrade();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractGameAction.AttackEffect effect;
        if (baseDamage >= 30)
            effect = AbstractGameAction.AttackEffect.SLASH_HEAVY;
        else
            effect = AbstractGameAction.AttackEffect.SLASH_DIAGONAL;
        addToBot( new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), effect));
        while(upgraded)
            downgrade();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        upgradeDamage(UPGRADE_PLUS_DMG + 2*timesUpgraded);
        upgradeName();
    }

    @Override
    public void downgrade() {
        super.downgrade();
        baseDamage -= (UPGRADE_PLUS_DMG + 2*timesUpgraded);
    }
}
