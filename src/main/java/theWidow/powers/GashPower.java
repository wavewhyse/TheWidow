package theWidow.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makePowerPath;


public class GashPower extends TwoAmountPower implements CloneablePowerInterface, NonStackablePower {

    public static final String POWER_ID = WidowMod.makeID("GashPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("GashPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("GashPower32.png"));

    //private static int gashIDOffset;

    public GashPower(final AbstractCreature owner, final int damage, final int duration) {
        name = NAME;
        ID = POWER_ID;// + gashIDOffset;
        //gashIDOffset++;

        this.owner = owner;
        this.amount = duration;
        this.amount2 = damage;

        type = PowerType.DEBUFF;
        isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        flash();
        addToBot(new DamageAction(owner, new DamageInfo(owner, amount2, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (amount > 1)
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        else
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + amount2 + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new GashPower(owner, amount2, amount);
    }
}
