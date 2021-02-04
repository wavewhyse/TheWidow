package theWidow.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theWidow.vfx.UpgradeHammerHit;

public class UpgradeDrawnCardAction extends AbstractGameAction {

    private static final float DURATION = Settings.ACTION_DUR_XFAST;

    public UpgradeDrawnCardAction() {
        actionType = ActionType.CARD_MANIPULATION;
        duration = DURATION;
    }

    @Override
    public void update() {
        if (duration == DURATION) {
            AbstractCard c = AbstractDungeon.player.hand.getTopCard();
            if (c.canUpgrade()) {
                c.upgrade();
                AbstractDungeon.effectsQueue.add(new UpgradeHammerHit(c));
            }
        }
        tickDuration();
    }
}
