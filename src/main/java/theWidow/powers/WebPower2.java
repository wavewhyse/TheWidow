package theWidow.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.cards.HermitWeave;
import theWidow.util.TextureLoader;

import java.util.ArrayList;

import static theWidow.WidowMod.makePowerPath;

public class WebPower2 extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = WidowMod.makeID(WebPower2.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("WebPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("WebPower32.png"));
    private ArrayList<PowerTip> tips;

    public WebPower2(final AbstractCreature owner, final int amount) {
        name = powerStrings.NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        tips = new ArrayList<>();
        tips.add(new PowerTip(powerStrings.DESCRIPTIONS[0], powerStrings.DESCRIPTIONS[1]));

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            flashWithoutSound();
            if (!owner.hasPower(HermitWeave.HermitWeavePower.POWER_ID))
                AbstractDungeon.actionManager.addToTop(new ReducePowerAction(owner, owner, this, 1));
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(info.owner, owner, new CaughtPower(info.owner, amount), amount));

        }
        return damageAmount;
    }

    @Override
    public AbstractPower makeCopy() {
        return new WebPower2(owner, amount);
    }
}
