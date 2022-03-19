package theWidow.deprecated;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.util.Wiz;


@Deprecated
public class TransensionPower2 extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = WidowMod.makeID(TransensionPower2.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public TransensionPower2(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = powerStrings.NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = -1;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void onInitialApplication() {
        for (AbstractCard c : Wiz.adp().hand.group) {
            c.setCostForTurn(-9);
        }
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        AbstractDungeon.transformCard(card);
        AbstractCard newCard = AbstractDungeon.transformedCard.makeCopy();
        newCard.setCostForTurn(-9);
        Wiz.adp().hand.removeCard(card);
        Wiz.adp().hand.addToTop(newCard);
    }

    @Override
    public AbstractPower makeCopy() {
        return new TransensionPower2(owner, source, amount);
    }
}
