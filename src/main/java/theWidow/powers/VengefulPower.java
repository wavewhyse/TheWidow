package theWidow.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makePowerPath;


public class VengefulPower extends AbstractPower implements CloneablePowerInterface, OnReceivePowerPower {

    public static final String POWER_ID = WidowMod.makeID("VengefulPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("VengefulPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("VengefulPower32.png"));

    private boolean negateOwnDebuff;

    public VengefulPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        negateOwnDebuff = false;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power instanceof StrengthPower && power.amount < 0 && target.hasPower(LoseStrengthPower.POWER_ID))
            return true;
        if (power.type == PowerType.DEBUFF && !target.hasPower(ArtifactPower.POWER_ID)) {
            if (negateOwnDebuff) {
                negateOwnDebuff = false;
                return true;
            }
            negateOwnDebuff = true;
            addToTop(new ApplyPowerAction(owner, owner, new LoseStrengthPower(owner, amount), amount));
            addToTop(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount), amount));
        }
        return true;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new VengefulPower(owner, amount);
    }
}
