package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
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

public class GeminiBot extends CustomCard {

    public static final String ID = WidowMod.makeID(GeminiBot.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("GeminiBot.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = -2;

    private AbstractCard cardToCopy;
    private int storedEnergyOnUse;

    public GeminiBot() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
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
        if (cardToCopy != null) {
            if (upgraded && cardToCopy.canUpgrade())
                cardToCopy.upgrade();
//            cardToCopy.dontTriggerOnUseCard = true;
            cardToCopy.purgeOnUse = true;
            cardToCopy.energyOnUse = storedEnergyOnUse;
//            addToTop(new NewQueueCardAction(cardToCopy, m, true, true));
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(cardToCopy, m, storedEnergyOnUse, true, true), true);
            cardToCopy = null;
        }
    }

    @Override
    public void triggerOnExhaust() {
        cardToCopy = null;
        AbstractDungeon.actionManager.removeFromQueue(this);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        dontTriggerOnUseCard = true;
        cardToCopy = c.makeCopy();
        cardToCopy.misc = c.misc;
        addToBot(new NewQueueCardAction(this, AbstractDungeon.actionManager.cardQueue.get(0).monster, true, true));
        storedEnergyOnUse = c.energyOnUse;
    }

    @Override
    public void upgrade() {
        upgradeName();
        initializeDescription();
    }
}
