package theWidow.cards;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makePowerPath;

public class Scrapper extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Scrapper.class.getSimpleName());
    public static final String IMG = makeCardPath("Scrapper.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int AMOUNT = 3;
    private static final int UPGRADE_PLUS_AMOUNT = 1;

    // /STAT DECLARATION/

    public Scrapper() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot( new ApplyPowerAction(p, p, new ScrapperPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_AMOUNT);
            initializeDescription();
        }
    }

    public static class ScrapperPower extends AbstractPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(ScrapperPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ScrapperPower84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ScrapperPower32.png"));

        public ScrapperPower(final AbstractCreature owner, final int amount) {
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
        public void updateDescription() {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        }

        @Override
        public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
            int count = 0;
            for (AbstractPower p : owner.powers) {
                if (p.type == PowerType.DEBUFF)
                    count++;
            }
            if (count > 0) {
                flash();
                addToBot(new GainBlockAction(owner, count * amount));
            }
        }

        @Override
        public AbstractPower makeCopy() {
            return new ScrapperPower(owner, amount);
        }
    }
}
