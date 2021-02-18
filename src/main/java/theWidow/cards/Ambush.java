package theWidow.cards;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makePowerPath;

public class Ambush extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Ambush.class.getSimpleName());
    public static final String IMG = makeCardPath("Ambush.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int  UPGRADED_COST = 1;

    // /STAT DECLARATION/

    public Ambush() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new AmbushPower(p, 1), 0));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }

    public static class AmbushPower extends AbstractPower implements CloneablePowerInterface{

        public static final String POWER_ID = WidowMod.makeID(AmbushPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Ambush84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Ambush32.png"));

        public AmbushPower(final AbstractCreature owner, final int amount) {
            name = powerStrings.NAME;
            ID = POWER_ID;

            this.owner = owner;
            this.amount = -1;

            type = PowerType.BUFF;
            isTurnBased = false;

            this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

            updateDescription();
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0];
        }

        @Override
        public void atEndOfTurn(boolean isPlayer) {
            if (isPlayer && AbstractDungeon.actionManager.cardsPlayedThisTurn.stream().noneMatch(c -> c.type == CardType.ATTACK)) {
                flash();
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (!c.isEthereal)
                        c.retain = true;
                }
            }
        }

        @Override
        public AbstractPower makeCopy() {
            return new AmbushPower(owner, amount);
        }
    }
}
