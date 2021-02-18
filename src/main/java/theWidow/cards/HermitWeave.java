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
import theWidow.powers.WebPower;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makePowerPath;

public class HermitWeave extends CustomCard {

    public static final String ID = WidowMod.makeID(HermitWeave.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("HermitWeave.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 3;
    private static final int UPGRADED_COST = 2;

    // /STAT DECLARATION/

    public HermitWeave() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }
    
    // Actions the card should do.
    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new HermitWeavePower(p, 1), 1 ));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }

    public static class HermitWeavePower extends AbstractPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(HermitWeavePower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("HermitWeavePower84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("HermitWeavePower32.png"));

        public HermitWeavePower(final AbstractCreature owner, final int amount) {
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
            addToBot(new ApplyPowerAction(owner, owner, new WebPower(owner, amount), amount ) );
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        }

        @Override
        public AbstractPower makeCopy() {
            return new HermitWeavePower(owner, amount);
        }
    }
}