package theWidow.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makePowerPath;

public class GrenadierPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = WidowMod.makeID(GrenadierPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("GrenadierPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("GrenadierPower32.png"));

    public GrenadierPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        addSlots(amount);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        addSlots(stackAmount);
    }

    private void addSlots(int num) {
        AbstractDungeon.player.potionSlots += num;
        for (int i = 0; i < num; i++) {
            int finalI = i;
            addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    AbstractDungeon.player.potions.add(new PotionSlot(AbstractDungeon.player.potionSlots - 1 - finalI));
                    isDone = true;
                }
            });
        }
        /*addToBot(new WaitAction(10f));
        for (int i = 0; i < num; i++)
            addToBot(new ObtainPotionAction(new GrenadePotion()));*/
        //AbstractDungeon.player.adjustPotionPositions();
    }

    @Override
    public void onVictory() {
        AbstractPlayer p = AbstractDungeon.player;
        p.potionSlots -= amount;
        /*
            A case where arrays starting at 0 SUCKS and IS CONFUSING
            Okay so, this starts at the *last index* of the potions arraylist: size - 1.
            It decrements until it is at the index equal to the *new* size that
            the array SHOULD be. And it removes that, leaving all indices intact
            from 0 through the new size minus 1; which is correct.
            Why the FDUCK is potionSlots not just equal to potions.size() anyway i hate this
        */
        if (p.potions.size() > p.potionSlots) {
            p.potions.subList(p.potionSlots, p.potions.size()).clear();
        }
    }

    @Override
    public void updateDescription() {
        if (amount == 1)
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        else
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new GrenadierPower(owner, amount);
    }
}
