package theWidow.cards;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;
import theWidow.vfx.ShockEffect;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makePowerPath;

public class Disruptor extends CustomCard {

    public static final String ID = WidowMod.makeID(Disruptor.class.getSimpleName());
    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("ShockEmitters.png");// "public static final String IMG = makeCardPath("Disruptor.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int DURATION = 1;
    private static final int UPGRADE_PLUS_DURATION = 1;

    public Disruptor() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DURATION;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
        addToBot(new VFXAction(new ShockEffect(m.hb.cX, m.hb.cY)));
        addToBot(new VFXAction(new ShockEffect(m.hb.cX, m.hb.cY)));
        addToBot(new VFXAction(new ShockEffect(m.hb.cX, m.hb.cY)));
        addToBot(new VFXAction(new ShockEffect(m.hb.cX, m.hb.cY)));
        addToBot( new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn)));
        addToBot( new ApplyPowerAction(m, p, new DisruptorPower(m, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_DURATION);
            initializeDescription();
        }
    }

    public static class DisruptorPower extends AbstractPower implements CloneablePowerInterface, OnReceivePowerPower {

        public static final String POWER_ID = WidowMod.makeID(DisruptorPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShockedPower84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShockedPower32.png"));

        public DisruptorPower(final AbstractCreature owner, final int amount) {
            name = powerStrings.NAME;
            ID = POWER_ID;

            this.owner = owner;
            this.amount = amount;

            type = PowerType.DEBUFF;
            isTurnBased = true;

            this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

            updateDescription();
        }

        @Override
        public boolean onReceivePower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
            return !(p instanceof StrengthPower && p.amount > 0);
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0];
        }

        @Override
        public void atEndOfRound() {
            if (amount <= 1)
                addToBot(new RemoveSpecificPowerAction(owner, AbstractDungeon.player, this));
            else
                addToBot(new ReducePowerAction(owner, AbstractDungeon.player, this, 1));
        }

        @Override
        public AbstractPower makeCopy() {
            return new DisruptorPower(owner, amount);
        }
    }
}
