package theWidow.powers;

import basemod.BaseMod;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeManagerAction;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makePowerPath;


public class TranscensionPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = WidowMod.makeID(TranscensionPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("TranscensionPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("TranscensionPower32.png"));

    public TranscensionPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        if(amount == 1)
            description = DESCRIPTIONS[0] + DESCRIPTIONS[3];
        else
            description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + amount + DESCRIPTIONS[3];
    }

    @Override
    public void atStartOfTurnPostDraw() {
        flash();
        for (int i=0; i<amount; i++)
            addToBot(new WidowUpgradeManagerAction((AbstractPlayer) owner, true, BaseMod.MAX_HAND_SIZE));
    }

    @Override
    public AbstractPower makeCopy() {
        return new TranscensionPower(owner, source, amount);
    }
}
