package theWidow.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import theWidow.cards.Downgradeable;

public class WidowDowngradeCardAction extends AbstractGameAction {

    private AbstractPlayer p = AbstractDungeon.player;
    private AbstractCard c;
    private boolean permanent;
    public float DURATION = 0.1f;

    public WidowDowngradeCardAction(AbstractCard card, boolean permanent) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = DURATION;
        this.c = card;
        this.permanent = permanent;
    }

    public WidowDowngradeCardAction(AbstractCard card) {
        this(card, false);
    }

    @Override
    public void update() {
        if (duration == DURATION) {
            if (permanent)
                p.masterDeck.group.stream().filter(dc -> dc.uuid.equals(c.uuid) && dc.upgraded).forEach(dc -> {
                    doDowngrade(dc);
                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(dc.makeStatEquivalentCopy()));
                    AbstractDungeon.effectsQueue.add(new ExhaustCardEffect(dc));
                    addToTop(new WaitAction(Settings.ACTION_DUR_MED));
                });
            doDowngrade(c);
//            AbstractDungeon.effectsQueue.add(new ExhaustCardEffect(c));
            c.superFlash(new Color(0.5f,0f,0.1f,0f));
        }
        tickDuration();
    }

    private static void doDowngrade(AbstractCard card) {
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
                if (downgradedVersion.cost >= 0)
                    card.updateCost(downgradedVersion.cost - card.cost);
                card.purgeOnUse = downgradedVersion.purgeOnUse;
                card.isEthereal = downgradedVersion.isEthereal;
                card.exhaust = downgradedVersion.exhaust;
                card.retain = downgradedVersion.retain;

                card.timesUpgraded = 0;
                card.upgraded = card.upgradedBlock = card.upgradedCost = card.upgradedDamage = card.upgradedMagicNumber = false;

                card.name = downgradedVersion.name;
                card.rawDescription = downgradedVersion.rawDescription;
            }
            card.applyPowers();
            card.initializeDescription();
            //card.initializeTitle(); <------find a way to do this (maybe)!
        }
    }
}
