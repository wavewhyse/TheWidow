package theWidow.deprecated;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
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
import theWidow.powers.AbstractEasyPower;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
@Deprecated
public class DischargeBattery extends CustomCard {

    public static final String ID = WidowMod.makeID(DischargeBattery.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("DischargeBattery");

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
//        Wiz.apply(new EnergyDownPower(p, magicNumber));
        Wiz.apply(new DischargeBatteryPower(p, 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }

    public static class DischargeBatteryPower extends AbstractEasyPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(DischargeBatteryPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        public DischargeBatteryPower(final AbstractCreature owner, final int amount) {
super( powerStrings.NAME,
                    PowerType.BUFF,
                    owner,
                    amount );
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
                Wiz.adp().energy.use(energy);
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    Wiz.apply(new ParalysisPower2(m, energy * amount));
                    Wiz.apply(new ShockedPower(m, energy * amount));
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
