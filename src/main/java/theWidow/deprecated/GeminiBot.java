package theWidow.deprecated;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
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
public class GeminiBot extends BetaCard {

    public static final String ID = WidowMod.makeID(GeminiBot.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Skill.png");// "public static final String IMG = makeCardPath("GeminiBot.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = -2;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int BLOCK = 3;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    public GeminiBot() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseBlock = BLOCK;
        magicNumber = baseMagicNumber = DAMAGE;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SewingKitRelic.ID))
            upgrade();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        cantUseMessage = "I can't play that.";
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageRandomEnemyAction(new DamageInfo(p, magicNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new GainBlockAction(p, magicNumber));
        //while (upgraded) downgrade();
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

    @Override
    public void upgrade() {
        upgradeMagicNumber(UPGRADE_PLUS_DMG );
        upgradeName();
    }

    @Override
    public void downgrade() {
        super.downgrade();
        magicNumber = baseMagicNumber -= ( UPGRADE_PLUS_DMG );
    }
}
