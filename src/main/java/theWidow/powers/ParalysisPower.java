package theWidow.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ReactivePower;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makePowerPath;


public class ParalysisPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = WidowMod.makeID(ParalysisPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ParalysisPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ParalysisPower32.png"));

    public boolean primed;

    public ParalysisPower(final AbstractCreature owner, final int amount) {
        name = powerStrings.NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.DEBUFF;
        isTurnBased = true;
        primed = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void onInitialApplication() {
        if (owner instanceof AbstractMonster && !owner.isDying) {
            ((AbstractMonster) owner).createIntent();
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (owner.hasPower(ReactivePower.POWER_ID) && !owner.isDying)
            ((AbstractMonster) owner).createIntent();
        return damageAmount;
    }

    @Override
    public void onRemove() {
        if (owner instanceof AbstractMonster && !owner.isDying) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    ((AbstractMonster) owner).createIntent();
                    isDone = true;
                }
            });
        }
    }

    @Override
    public void atEndOfRound() {
        if (amount <= 1)
            addToBot(new RemoveSpecificPowerAction(owner, AbstractDungeon.player, this));
        else {
            addToBot(new ReducePowerAction(owner, AbstractDungeon.player, this, 1));
            primed = false;
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new ParalysisPower(owner, amount);
    }
}
