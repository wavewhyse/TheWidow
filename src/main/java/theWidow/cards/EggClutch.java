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
import theWidow.util.TextureLoader;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makePowerPath;

public class EggClutch extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(EggClutch.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("EggClutch.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int UPGRADED = 1;
    private static final int UPGRADE_PLUS_UPGRADED = 1;

    // /STAT DECLARATION/

    public EggClutch() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = UPGRADED;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot( new ApplyPowerAction(p, p, new EggClutchPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_UPGRADED);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    public static class EggClutchPower extends AbstractPower implements CloneablePowerInterface {

        public static final String POWER_ID = WidowMod.makeID("EggClutchPower");
        private static final PowerStrings powerStrings = languagePack.getPowerStrings(POWER_ID);
        public static final String NAME = powerStrings.NAME;
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("EggClutchPower84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("EggClutchPower32.png"));

        public EggClutchPower(final AbstractCreature owner, final int amount) {
            name = NAME;
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
        public void updateDescription() {
            if (amount == 1)
                description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DESCRIPTIONS[3];
            else
                description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3];
        }

        @Override
        public void onVictory() {
            flash();
            WidowMod.EGG_CLUTCH_UPGRADES = amount;
        }

        @Override
        public AbstractPower makeCopy() {
            return new EggClutchPower(owner, amount);
        }
    }
}
