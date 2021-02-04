package theWidow.deprecated;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makePowerPath;


public class TransensionPower2 extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = WidowMod.makeID(TransensionPower2.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("TransensionPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("TransensionPower32.png"));

    public TransensionPower2(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = -1;
        this.source = source;

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
    public void onInitialApplication() {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            c.setCostForTurn(-9);
        }
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        AbstractDungeon.transformCard(card);
        AbstractCard newCard = AbstractDungeon.transformedCard.makeCopy();
        newCard.setCostForTurn(-9);
        AbstractDungeon.player.hand.removeCard(card);
        AbstractDungeon.player.hand.addToTop(newCard);
    }

    @Override
    public AbstractPower makeCopy() {
        return new TransensionPower2(owner, source, amount);
    }
}
