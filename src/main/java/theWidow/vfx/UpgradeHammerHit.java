package theWidow.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.UpgradeHammerImprintEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineParticleEffect;

public class UpgradeHammerHit extends AbstractGameEffect {
    private final AbstractCard card;

    public UpgradeHammerHit(AbstractCard c) {
        this.card = c;
        this.duration = 0.2F;
    }

    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            AbstractDungeon.actionManager.addToTop(new SFXAction("HammerHit"));
            AbstractDungeon.topLevelEffectsQueue.add(new UpgradeHammerImprintEffect(card.hb.cX -80.0F * Settings.scale, card.hb.cY));
            if (!Settings.DISABLE_EFFECTS) {
                for (int i = 0; i < 10; i++)
                    AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineParticleEffect(card.hb.cX +
                            MathUtils.random(-10.0F, 10.0F) * Settings.scale, card.hb.cY +
                            MathUtils.random(-10.0F, 10.0F) * Settings.scale));
            }
            isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) { }

    @Override
    public void dispose() { }
}
