package theWidow.cards;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makePowerPath;

public class Gash extends CustomCard {

    public static final String ID = WidowMod.makeID(Gash.class.getSimpleName());
    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("Gash.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = -1;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DMG = 1;

    public Gash() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot( new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int effect = EnergyPanel.totalCount;
                if (energyOnUse != -1)
                    effect = energyOnUse;
                if (p.hasRelic(ChemicalX.ID)) {
                    effect += ChemicalX.BOOST;
                    p.getRelic(ChemicalX.ID).flash();
                }
                if (effect > 0)
                    addToTop(new ApplyPowerAction(m, p, new GashPower(m, damage*2, effect), effect));
                if (!freeToPlayOnce)
                    p.energy.use(EnergyPanel.totalCount);
                isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }

    public static class GashPower extends TwoAmountPower implements CloneablePowerInterface, NonStackablePower {
        public static final String POWER_ID = WidowMod.makeID("GashPower");
        private static final PowerStrings powerStrings = languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("GashPower84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("GashPower32.png"));

        //private static int gashIDOffset;

        public GashPower(final AbstractCreature owner, final int damage, final int duration) {
            name = powerStrings.NAME;
            ID = POWER_ID;// + gashIDOffset;
            //gashIDOffset++;

            this.owner = owner;
            this.amount = duration;
            this.amount2 = damage;

            type = PowerType.DEBUFF;
            isTurnBased = true;

            this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

            updateDescription();
        }

        @Override
        public void atEndOfRound() {
            flash();
            addToBot(new DamageAction(owner, new DamageInfo(owner, amount2, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            if (amount > 1)
                addToBot(new ReducePowerAction(owner, owner, this, 1));
            else
                addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + amount2 + DESCRIPTIONS[2];
        }

        @Override
        public AbstractPower makeCopy() {
            return new GashPower(owner, amount2, amount);
        }
    }
}
