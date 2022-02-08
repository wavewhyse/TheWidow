package theWidow.cards;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.CaughtPower;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makePowerPath;

public class SadisticIntent extends CustomCard {

    public static final String ID = WidowMod.makeID(SadisticIntent.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("SadisticIntent.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int CAUGHT = 4;
    private static final int UPGRADE_PLUS_CAUGHT = 2;

    public SadisticIntent() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = CAUGHT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot( new ApplyPowerAction(p, p, new SadisticIntentPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_CAUGHT);
        }
    }

    public static class SadisticIntentPower extends AbstractPower implements CloneablePowerInterface {

        public static final String POWER_ID = WidowMod.makeID(SadisticIntentPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("SadisticIntentPower84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("SadisticIntentPower32.png"));

        private boolean myDebuff;

        public SadisticIntentPower(final AbstractCreature owner, final int amount) {
            name = powerStrings.NAME;
            ID = POWER_ID;

            this.owner = owner;
            this.amount = amount;

            type = PowerType.BUFF;
            isTurnBased = false;

            myDebuff = false;

            this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

            updateDescription();
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
            if (power.type == PowerType.DEBUFF && target instanceof AbstractMonster && source == owner && !target.hasPower(ArtifactPower.POWER_ID)) {
                if (power instanceof CaughtPower && myDebuff) {
                    myDebuff = false;
                    return;
                }
                flash();
                addToTop(new ApplyPowerAction(target, source, new CaughtPower(target, amount)));
                myDebuff = true;
//                addToTop(new ApplyPowerAction(owner, owner, new VigorPower(owner, amount), amount));
    //            addToTop(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount), amount));
    //            addToTop(new ApplyPowerAction(owner, owner, new LoseStrengthPower(owner, amount), amount));
    //            addToTop(new ApplyPowerAction(owner, owner, new DexterityPower(owner, amount), amount));
    //            addToTop(new ApplyPowerAction(owner, owner, new LoseDexterityPower(owner, amount), amount));
//                addToBot(new ApplyPowerAction(owner, source, new DrawCardNextTurnPower(owner, amount), amount, true));
            }
        }

        /*@Override
        public void atStartOfTurn() {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                boolean thisMon = false;
                for (AbstractPower pow : m.powers) {
                    if (pow.type == PowerType.DEBUFF) {
                        thisMon = true;
                        break;
                    }
                }
                if (!thisMon)
                    return;
            }
            flash();
            addToBot(new DrawCardAction(amount));
        }*/

        @Override
        public AbstractPower makeCopy() {
            return new SadisticIntentPower(owner, amount);
        }
    }
}
