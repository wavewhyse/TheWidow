package theWidow.cards;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;
import theWidow.vfx.ShockEffect;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makePowerPath;

public class Tazer extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Tazer.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = WidowMod.makeCardPath("Attack.png");// "public static final String IMG = WidowMod.makeCardPath("Tazer.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = theWidow.TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 4;

    // /STAT DECLARATION/

    public Tazer() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        if (CardLibrary.getAllCards() != null && !CardLibrary.getAllCards().isEmpty())
            theWidow.util.artHelp.CardArtRoller.computeCard(this);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot( new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot( new ApplyPowerAction(m, p, new TazerPower(m, 3), 3));
    }

    public static class TazerPower extends AbstractPower implements CloneablePowerInterface, NonStackablePower {
        public static final String POWER_ID = WidowMod.makeID("TazerPower");
        private static final PowerStrings powerStrings = languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("TazerPower84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("TazerPower32.png"));

        public TazerPower(final AbstractMonster owner, final int duration) {
            name = powerStrings.NAME;
            ID = POWER_ID;

            this.owner = owner;
            this.amount = duration;

            type = PowerType.DEBUFF;
            isTurnBased = true;

            this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

            updateDescription();
        }

        @Override
        public void atEndOfRound() {
            if (amount <= 1) {
                addToBot(new RemoveSpecificPowerAction(owner, owner, this));
                addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
                addToBot(new VFXAction(new ShockEffect(owner.hb.cX, owner.hb.cY)));
                addToBot(new StunMonsterAction((AbstractMonster) owner, AbstractDungeon.player));
            } else
                addToBot(new ReducePowerAction(owner, owner, this, 1));
        }

        @Override
        public void updateDescription() {
            if (amount == 1)
                description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
            else
                description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }

        @Override
        public AbstractPower makeCopy() {
            return new TazerPower((AbstractMonster) owner, amount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
