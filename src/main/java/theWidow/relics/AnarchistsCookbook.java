package theWidow.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.helpers.PowerTip;
import theWidow.WidowMod;
import theWidow.potions.GrenadePotion;
import theWidow.util.TexLoader;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeRelicOutlinePath;
import static theWidow.WidowMod.makeRelicPath;

public class AnarchistsCookbook extends CustomRelic {
    public static final String ID = WidowMod.makeID(AnarchistsCookbook.class.getSimpleName());

    public AnarchistsCookbook() {
        super( ID,
                TexLoader.getTexture(makeRelicPath(AnarchistsCookbook.class.getSimpleName())),
                TexLoader.getTexture(makeRelicOutlinePath(AnarchistsCookbook.class.getSimpleName())),
                RelicTier.COMMON,
                LandingSound.FLAT );
        tips.add(new PowerTip(BaseMod.getKeywordTitle(DESCRIPTIONS[1]), BaseMod.getKeywordDescription(DESCRIPTIONS[1])));
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(Wiz.adp(), this));
        addToBot(new ObtainPotionAction(new GrenadePotion()));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
