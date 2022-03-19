package theWidow.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
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
import theWidow.util.TexLoader;
import theWidow.util.Wiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class FluxCatalyst extends CustomRelic {
    public static final String ID = WidowMod.makeID(FluxCatalyst.class.getSimpleName());

    public FluxCatalyst() {
        super( ID,
                TexLoader.getTexture(makeRelicPath("placeholder_relic2")),
                TexLoader.getTexture(makeRelicOutlinePath("placeholder_relic2")),
                RelicTier.SHOP,
                LandingSound.FLAT );
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
                ArrayList<AbstractCard> upgradableCards = Wiz.adp().masterDeck.group.stream().filter(AbstractCard::canUpgrade).collect(Collectors.toCollection(ArrayList::new));

                Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
                for (int i=0; i < 3 && i < upgradableCards.size(); i++) {
                    AbstractCard card = upgradableCards.get(i);
                    card.upgrade();
                    Wiz.adp().bottledCardUpgradeCheck(card);
                    AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy(),
                            (float) Settings.WIDTH / 2.0F - (190.0f + 190f * i) * Settings.scale,
                            (float) Settings.HEIGHT / 2.0F));
                }
            }
            
            duration -= Gdx.graphics.getDeltaTime();
            if (duration < 0.0F) {
                isDone = true;
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