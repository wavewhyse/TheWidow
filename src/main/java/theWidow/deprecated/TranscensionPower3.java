package theWidow.deprecated;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makePowerPath;


@Deprecated
public class TranscensionPower3 extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = WidowMod.makeID(TranscensionPower3.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("TranscensionPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("TranscensionPower32.png"));

    private final boolean upgraded;

    public TranscensionPower3(final AbstractCreature owner, int amount, boolean upgraded) {
        name = powerStrings.NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.upgraded = upgraded;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurnPostDraw() {
        boolean willItBeAdded = AbstractDungeon.player.hand.size() < 10;
        addToBot(new DiscoveryAction(false, amount));
        /*if (willItBeAdded) {
            if (AbstractDungeon.player.hand.getTopCard().canUpgrade())
                AbstractDungeon.player.hand.getTopCard().upgrade();
        } else {
            if (AbstractDungeon.player.discardPile.getTopCard().canUpgrade())
                AbstractDungeon.player.discardPile.getTopCard().upgrade();
        }*/
    }

    @Override
    public AbstractPower makeCopy() {
        return new TranscensionPower3(owner, amount, upgraded);
    }
}
