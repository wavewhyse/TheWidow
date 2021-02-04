package theWidow.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makePowerPath;


public class DischargeBatteryPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = WidowMod.makeID(DischargeBatteryPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("DischargeBatteryPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("DischargeBatteryPower32.png"));

    private boolean upgraded;

    public DischargeBatteryPower(final AbstractCreature owner, final int amount, final boolean upgraded) {
        name = NAME;
        if (upgraded)
            ID = POWER_ID + 1;
        else
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
        if (upgraded)
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DESCRIPTIONS[2];
        else
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DESCRIPTIONS[3];
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (upgraded) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                addToBot(new ApplyPowerAction(m, owner, new ParalysisPower(m, amount)));
                addToBot(new ApplyPowerAction(m, owner, new WeakPower(m, amount, false)));
            }
        } else {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            addToBot(new ApplyPowerAction(m, owner, new ParalysisPower(m, amount)));
            addToBot(new ApplyPowerAction(m, owner, new WeakPower(m, amount, false)));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new DischargeBatteryPower(owner, amount, upgraded);
    }
}
