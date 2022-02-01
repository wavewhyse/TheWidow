package theWidow.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import theWidow.vfx.UpgradeHammerHit;

public class WidowUpgradeCardAction extends AbstractGameAction {
    private AbstractPlayer p = AbstractDungeon.player;
    private AbstractCard c;
    private boolean permanent;
    public float DURATION = 0.1f;

    public WidowUpgradeCardAction(AbstractCard card, boolean permanent) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = DURATION;
        this.c = card;
        this.permanent = permanent;
    }

    public WidowUpgradeCardAction(AbstractCard card) {
        this(card, false);
    }

    @Override
    public void update() {
        if (duration == DURATION) {
            if (permanent) {
                for (AbstractCard dc : p.masterDeck.group) {
                    if (!dc.uuid.equals(c.uuid))
                        continue;
                    if (dc.canUpgrade()) {
                        dc.upgrade();
                        AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(dc.makeStatEquivalentCopy()));
                        addToTop(new WaitAction(Settings.ACTION_DUR_MED));
                    }
                    break;
                }
            }
            if (c.canUpgrade()) {
                c.upgrade();
                AbstractDungeon.effectsQueue.add(new UpgradeHammerHit(c));
                c.applyPowers();
            }
        }
        tickDuration();
    }
}
