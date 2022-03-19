package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class GeminiBot extends CustomCard {
    public static final String ID = WidowMod.makeID(GeminiBot.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private AbstractCard cardToCopy;
    private int storedEnergyOnUse;

    public GeminiBot() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(GeminiBot.class.getSimpleName()),
                -2,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.NONE );
        exhaust = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
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
            Wiz.adam().addCardQueueItem(new CardQueueItem(cardToCopy, m, storedEnergyOnUse, true, true), true);
            cardToCopy = null;
        }
    }

    @Override
    public void triggerOnExhaust() {
        cardToCopy = null;
        Wiz.adam().removeFromQueue(this);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        dontTriggerOnUseCard = true;
        if (cardToCopy == null) {
            cardToCopy = c.makeCopy();
            cardToCopy.misc = c.misc;
            addToTop(new NewQueueCardAction(this, Wiz.adam().cardQueue.get(0).monster, true, true));
            storedEnergyOnUse = c.energyOnUse;
        }
    }

    @Override
    public void upgrade() {
        upgradeName();
        initializeDescription();
    }
}
