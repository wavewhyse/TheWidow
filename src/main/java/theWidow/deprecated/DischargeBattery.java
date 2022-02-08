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
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.ParalysisPower2;
import theWidow.powers.ShockedPower;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makePowerPath;

@AutoAdd.Ignore
@Deprecated
public class DischargeBattery extends CustomCard {

    public static final String ID = WidowMod.makeID(DischargeBattery.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("DischargeBattery.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;

    public DischargeBattery() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        addToBot(new ApplyPowerAction(p, p, new EnergyDownPower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new DischargeBatteryPower(p, 1), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }

    public static class DischargeBatteryPower extends AbstractPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(DischargeBatteryPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("DischargeBatteryPower84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("DischargeBatteryPower32.png"));

        public DischargeBatteryPower(final AbstractCreature owner, final int amount) {
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
            if (amount == 1)
                description = DESCRIPTIONS[0]  + DESCRIPTIONS[3];
            else
                description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3];
        }

        @Override
        public void atEndOfTurn(boolean isPlayer) {
            if (isPlayer && EnergyPanel.totalCount > 0) {
                flash();
                int energy = EnergyPanel.totalCount;
                AbstractDungeon.player.energy.use(energy);
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new ParalysisPower2(m, energy * amount), energy * amount));
                    addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new ShockedPower(m, energy * amount), energy * amount));
                }
            }
        }

        /*

        @Override
        public void atStartOfTurnPostDraw() {
            if (upgraded) {
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    addToBot(new ApplyPowerAction(m, owner, new ParalysisPower(m, amount)));
                    addToBot(new ApplyPowerAction(m, owner, new WeakPower(m, amount, false)));
                }
            } else {
                AbstractMonster m = AbstractDungeon.getRandomMonster();
                addToBot(new ApplyPowerAction(m, owner, new ParalysisPower(m, amount)));
                addToBot(new ApplyPowerAction(m, owner, new WeakPower(m, amount, false)));
            }
        }
    */

        @Override
        public AbstractPower makeCopy() {
            return new DischargeBatteryPower(owner, amount);
        }
    }
}
