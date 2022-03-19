package theWidow.deprecated;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.util.Wiz;


@Deprecated
public class TranscensionPower3 extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = WidowMod.makeID(TranscensionPower3.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final boolean upgraded;

    public TranscensionPower3(final AbstractCreature owner, int amount, boolean upgraded) {
        name = powerStrings.NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.upgraded = upgraded;

        type = PowerType.BUFF;
        isTurnBased = false;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurnPostDraw() {
        boolean willItBeAdded = Wiz.adp().hand.size() < 10;
        addToBot(new DiscoveryAction(false, amount));
        /*if (willItBeAdded) {
            if (Wiz.adp().hand.getTopCard().canUpgrade())
                Wiz.adp().hand.getTopCard().upgrade();
        } else {
            if (Wiz.adp().discardPile.getTopCard().canUpgrade())
                Wiz.adp().discardPile.getTopCard().upgrade();
        }*/
    }

    @Override
    public AbstractPower makeCopy() {
        return new TranscensionPower3(owner, amount, upgraded);
    }
}
