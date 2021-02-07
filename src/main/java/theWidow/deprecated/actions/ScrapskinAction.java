package theWidow.deprecated.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import theWidow.deprecated.DowngradeCardAction;

import java.util.UUID;

@Deprecated
public class ScrapskinAction extends AbstractGameAction {
    private AbstractPlayer p;

    private UUID uuid;
    private final boolean upgraded;

    private static final float DURATION = Settings.ACTION_DUR_MED;

    public ScrapskinAction(final AbstractPlayer p, final UUID uuid, boolean upgraded) {
        this.p = p;
        this.uuid = uuid;
        this.upgraded = upgraded;
        actionType = ActionType.SPECIAL;
    }
    
    @Override
    public void update() {
        if (upgraded) {
            addToBot(new RemoveDebuffsAction(p));
            for (AbstractCard c : p.masterDeck.group) {
                if (!c.uuid.equals(this.uuid))
                    continue;
                if (c.upgraded) {
                    addToTop(new DowngradeCardAction(c));
                    AbstractCard display = c.makeStatEquivalentCopy();
                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(display));
                    AbstractDungeon.effectList.add(new ExhaustCardEffect(display));
                }
            }
            for (AbstractCard c : GetAllInBattleInstances.get(uuid)) {
                addToBot(new DowngradeCardAction(c));
                c.superFlash();
            }
        } else {
            for (AbstractCard c : p.masterDeck.group) {
                if (!c.uuid.equals(this.uuid))
                    continue;
                if (!c.upgraded) {
                    c.upgrade();
                    AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                    addToTop(new WaitAction(Settings.ACTION_DUR_MED));
                }
            }
            for (AbstractCard c : GetAllInBattleInstances.get(uuid))
                c.upgrade();
        }
        isDone = true;
    }
}
