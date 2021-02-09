package theWidow.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theWidow.WidowMod;
import theWidow.actions.WidowDowngradeCardAction;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class HoloProjectorRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = WidowMod.makeID("HoloProjectorRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("HoloProjector.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("HoloProjector.png"));

    public static final int DOWNGRADES = 2;

    public HoloProjectorRelic() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.CLINK);
    }


    @Override
    public void atTurnStartPostDraw() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        System.out.println(AbstractDungeon.player.hand.size());

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                CardGroup upgraded = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard c : AbstractDungeon.player.hand.group)
                    if (c.upgraded)
                        upgraded.addToTop(c);
                for (int i=0; i<DOWNGRADES && !upgraded.isEmpty(); i++) {
                    AbstractCard c = upgraded.getRandomCard(AbstractDungeon.cardRandomRng);
                    addToTop(new WidowDowngradeCardAction(c));
                    upgraded.removeCard(c);
                }
                isDone = true;
            }
        });
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DOWNGRADES + DESCRIPTIONS[1];
    }

}
