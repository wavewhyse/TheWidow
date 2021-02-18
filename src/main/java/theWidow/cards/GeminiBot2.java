package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.relics.SewingKitRelic;

import static theWidow.WidowMod.makeCardPath;

public class GeminiBot2 extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(GeminiBot2.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("GeminiBot.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = -2;
    //private static final int EXHAUSTIVE = 2;

    private AbstractCard cardToCopy;

    // /STAT DECLARATION/

    public GeminiBot2() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
        //ExhaustiveVariable.setBaseValue(this, EXHAUSTIVE);
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SewingKitRelic.ID))
            upgrade();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        cantUseMessage = "I can't play that.";
        return cardToCopy != null;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded && cardToCopy.canUpgrade())
            cardToCopy.upgrade();
        //cardToCopy.dontTriggerOnUseCard = true;
        cardToCopy.purgeOnUse = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(cardToCopy, m, energyOnUse, true, true));
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        dontTriggerOnUseCard = true;
        cardToCopy = c.makeCopy();
        AbstractMonster m = null;
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (mon.hb.hovered)
                m = mon;
        }
        if (m == null)
            m = AbstractDungeon.getRandomMonster();
        AbstractMonster finalM = m;
        addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                if (!AbstractDungeon.player.hand.contains(GeminiBot2.this)) {
                    cardToCopy = null;
                    isDone = true;
                    return;
                }
                AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(GeminiBot2.this, finalM, c.energyOnUse, true, true));
                isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        upgradeName();
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
