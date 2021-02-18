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
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeManagerAction;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makePowerPath;

public class Neurojack extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Neurojack.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("Neurojack.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int UPGRADES = 1;
    private static final int UPGRADE_PLUS_UPGRADES = 1;

    // /STAT DECLARATION/

    public Neurojack() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = UPGRADES;
    }
    
    // Actions the card should do.
    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new NeurojackPower(p, magicNumber), magicNumber ));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_UPGRADES);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    public static class NeurojackPower extends AbstractPower implements CloneablePowerInterface {

        public static final String POWER_ID = WidowMod.makeID(NeurojackPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("NeurojackPower84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("NeurojackPower32.png"));

        public NeurojackPower(final AbstractCreature owner, final int amount) {
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

        @Override
        public void atStartOfTurnPostDraw() {
            flash();
            addToBot(new WidowUpgradeManagerAction(amount, true));
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
        public AbstractPower makeCopy() {
            return new NeurojackPower(owner, amount);
        }
    }
}