package theWidow.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makePowerPath;


public class SapPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = WidowMod.makeID("SapPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("SapPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("SapPower32.png"));

    public SapPower(final AbstractCreature owner, final int amount) {
        name = powerStrings.NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.DEBUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return damage - amount;
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL)
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    addToTop(new ReducePowerAction(owner, owner, SapPower.this, (amount+1)/2));
                    isDone = true;
                }
            });
        return damageAmount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer)
            addToBot(new ReducePowerAction(owner, owner, this, (amount+1)/2));
    }



    @Override
    public AbstractPower makeCopy() {
        return new SapPower(owner, amount);
    }
}
