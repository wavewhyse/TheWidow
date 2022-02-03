package theWidow.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.RunicDome;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import java.util.ArrayList;

import static theWidow.WidowMod.makePowerPath;


public class WebPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = WidowMod.makeID(WebPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("WebPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("WebPower32.png"));

    private static final Texture webBreakTex = TextureLoader.getTexture(makePowerPath("WebPower32.png"));
    private Hitbox wbhb;
    private ArrayList<PowerTip> tips;

    public static final float DAMAGE_MULT = 0.75f;

    public ArrayList<AbstractMonster> webbedMonsters;
    public AbstractMonster webBreakerMonster;

    public WebPower(final AbstractCreature owner, final int amount) {
        name = powerStrings.NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        webbedMonsters = new ArrayList<>();
        webBreakerMonster = null;
        tips = new ArrayList<>();
        tips.add(new PowerTip(powerStrings.DESCRIPTIONS[3], powerStrings.DESCRIPTIONS[4]));

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL)
            return damage * DAMAGE_MULT;
        else return damage;
    }

    public float atDamageReceiveButPassTheActualDamageSource(float damage, DamageInfo.DamageType type, AbstractMonster source) {
        if (type == DamageInfo.DamageType.NORMAL && webbedMonsters.contains(source))
            return damage * DAMAGE_MULT;
        else return damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL ) {
            flashWithoutSound();
            AbstractDungeon.actionManager.addToTop(new ReducePowerAction(owner, owner, this, 1));
        }
        return damageAmount;
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        updateWebbedMonsters();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateWebbedMonsters();
    }

    @Override
    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (target != null && target.isDying) {
                    updateWebbedMonsters();
                    for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                        m.applyPowers();
                }
                isDone = true;
            }
        });
    }

    public void updateWebbedMonsters() {
        webbedMonsters.clear();
        webBreakerMonster = null;
        int count = amount;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.isDeadOrEscaped())
                continue;
            switch (m.intent) {
                case ATTACK:
                case ATTACK_BUFF:
                case ATTACK_DEBUFF:
                case ATTACK_DEFEND:
                    break;
                default:
                    continue; //go to the next monster if this one isn't attacking
            }
            webbedMonsters.add(m);
            int multi = 0; //IntentMultiDmgField.amount.get(m);
            count -= Math.max(multi, 1);
            if (count < 0)  //if this monster is damaging BEYOND the web
                webBreakerMonster = m;
            if (count <= 0)
                break;  //out of web, dont bother with the other monsters
        }
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        if (webBreakerMonster != null) {
            if (wbhb == null)
                wbhb = new Hitbox(32f, 32f);
            wbhb.move(webBreakerMonster.hb.cX + webBreakerMonster.intentOffsetX + 64f * Settings.scale, webBreakerMonster.hb.cY + webBreakerMonster.hb_h / 2f - 16f + 64f * Settings.scale);
            wbhb.update();
        }
        else
            wbhb = null;
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, c);
        if (wbhb != null && !AbstractDungeon.player.hasRelic(RunicDome.ID) && !Settings.hideCombatElements) {
            sb.setColor(new Color(1, 1, 1, 1));
            sb.draw( webBreakTex, wbhb.x, wbhb.y, 16f,16f, 32f, 32f, Settings.scale * 1.5f, Settings.scale * 1.5f, 0f, 0, 0, 32, 32, false, false);
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "!?", wbhb.cX + 32f * Settings.scale, wbhb.cY + 18f * Settings.scale, new Color(1, 0, 0, 1));
            if (wbhb.hovered) {
                TipHelper.queuePowerTips(wbhb.cX + 32f, wbhb.cY, tips);
            }
            wbhb.render(sb);
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new WebPower(owner, amount);
    }
}
