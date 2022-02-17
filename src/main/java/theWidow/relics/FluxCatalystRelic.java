package theWidow.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class FluxCatalystRelic extends CustomRelic {

    public static final String ID = WidowMod.makeID("FluxCatalystRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic2.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic2.png"));

    public FluxCatalystRelic() {
        super(ID, IMG, OUTLINE, RelicTier.SHOP, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public static class FluxCatalystSmithEffect extends AbstractGameEffect {

        public FluxCatalystSmithEffect() {
            duration = startingDuration = Settings.ACTION_DUR_LONG;
            AbstractDungeon.overlayMenu.proceedButton.hide();
        }

        @Override
        public void update() {
            if (duration == startingDuration) {
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                ArrayList<AbstractCard> upgradableCards = new ArrayList();

                for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                    if (c.canUpgrade()) {
                        upgradableCards.add(c);
                    }
                }

                Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
                if (!upgradableCards.isEmpty()) {
                    if (upgradableCards.size() == 1) {
                        upgradableCards.get(0).upgrade();
                        AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
                        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(upgradableCards.get(0).makeStatEquivalentCopy()));
                    } else {
                        upgradableCards.get(0).upgrade();
                        upgradableCards.get(1).upgrade();
                        AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
                        AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(1));
                        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(upgradableCards.get(0).makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F - 190.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(upgradableCards.get(1).makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F + 190.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F));
                    }
                }
            }
            
            this.duration -= Gdx.graphics.getDeltaTime();

            if (this.duration < 0.0F) {
                this.isDone = true;
                if (CampfireUI.hidden) {
                    AbstractRoom.waitTimer = 0.0F;
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                    ((RestRoom)AbstractDungeon.getCurrRoom()).cutFireSound();
                }
            }
        }

        public void render(SpriteBatch spriteBatch) {

        }
        public void dispose() {

        }
    }
}