package theWidow.deprecated;

import basemod.AutoAdd;
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
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.WebPower2;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makePowerPath;

@AutoAdd.Ignore
@Deprecated
public class Lair extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Lair.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Lair.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int WEB = 2;
    private static final int UPGRADE_PLUS_WEB = 1;

    // /STAT DECLARATION/

    public Lair() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = WEB;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new LairPower(p, magicNumber), magicNumber));
    }

    public static class LairPower extends AbstractPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(LairPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("LairPower84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("LairPower32.png"));

        public LairPower(final AbstractCreature owner, final int amount) {
            name = powerStrings.NAME;
            ID = POWER_ID;

            this.owner = owner;
            this.amount = amount;

            type = PowerType.BUFF;
            isTurnBased = false;

            this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

            updateDescription();
        }

//        @Override
//        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
//            if(power instanceof WebPower && target == owner) {
//                flash();
//                power.amount += 2;
//            }
//        }

        @Override
        public void atStartOfTurnPostDraw() {
            flash();
            addToBot(new ApplyPowerAction(owner, owner, new WebPower2(owner, amount), amount ) );
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        }

        @Override
        public AbstractPower makeCopy() {
            return new LairPower(owner, amount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_WEB);
            initializeDescription();
        }
    }

//    @Override
//    public void downgrade() {
//        if (upgraded) {
//            name = cardStrings.NAME;
//            timesUpgraded--;
//            upgraded = false;
//            magicNumber = baseMagicNumber = WEB;
//            upgradedMagicNumber = false;
//        }
//    }
}