package theWidow.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import theWidow.WidowMod;
import theWidow.potions.GrenadePotion;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class AnarchistsCookbookRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = WidowMod.makeID("AnarchistsCookbookRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("AnarchistsCookbook.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("AnarchistsCookbook.png"));

    public AnarchistsCookbookRelic() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.FLAT);
        tips.add(new PowerTip("Grenade", GameDictionary.keywords.get("thewidow:Grenade")));
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ObtainPotionAction(new GrenadePotion()));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
