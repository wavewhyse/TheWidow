package theWidow.deprecated;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import theWidow.cards.Downgradeable;
public class DowngradeCardAction extends AbstractGameAction {

    private static final float DURATION = Settings.ACTION_DUR_FAST;

    private final AbstractCard card;

    public DowngradeCardAction() {
        this(null);
    }

    public DowngradeCardAction(AbstractCard card) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = DURATION;
        this.card = card;
    }

    @Override
    public void update() {
        if (card.upgraded) {
            if (card instanceof Downgradeable) {
                ((Downgradeable) card).downgrade();
            } else {
                AbstractCard downgradedVersion = card.makeCopy();
                card.baseDamage = card.damage = downgradedVersion.baseDamage;
                card.baseBlock = card.block = downgradedVersion.baseBlock;
                card.baseMagicNumber = card.magicNumber = downgradedVersion.baseMagicNumber;
                card.baseDiscard = card.discard = downgradedVersion.baseDiscard;
                card.baseDraw = card.draw = downgradedVersion.baseDraw;
                card.baseHeal = card.heal = downgradedVersion.baseHeal;
                card.cost = downgradedVersion.cost;
                card.costForTurn = downgradedVersion.costForTurn;
                card.purgeOnUse = downgradedVersion.purgeOnUse;
                card.isEthereal = downgradedVersion.isEthereal;
                card.exhaust = downgradedVersion.exhaust;
                card.retain = downgradedVersion.retain;

                card.timesUpgraded = 0;
                card.upgraded = card.upgradedBlock = card.upgradedCost = card.upgradedDamage = card.upgradedMagicNumber = false;

                card.name = CardCrawlGame.languagePack.getCardStrings(card.cardID).NAME;
                card.rawDescription = CardCrawlGame.languagePack.getCardStrings(card.cardID).DESCRIPTION;
            }
            card.applyPowers();
            card.initializeDescription();
            //card.initializeTitle(); <------find a way to do this (maybe)!
        }
        isDone = true;
    }
}
