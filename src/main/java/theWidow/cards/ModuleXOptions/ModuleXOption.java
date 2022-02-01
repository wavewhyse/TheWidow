package theWidow.cards.ModuleXOptions;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public abstract class ModuleXOption extends AbstractCard {
    protected ArrayList<AbstractCard> unchosenOptions;
    protected int remainingChoices;

    public ModuleXOption(ArrayList<AbstractCard> unchosenOptions, int remainingChoices, String id, String name, String imgUrl, String rawDescription) {
        super(id, name, imgUrl, -2, rawDescription, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        this.unchosenOptions = unchosenOptions;
        this.remainingChoices = remainingChoices;
    }

    @Override
    public void onChoseThisOption() {
        use(AbstractDungeon.player, null);
        if (remainingChoices > 0) {
            unchosenOptions.remove(this);
            for (AbstractCard c : unchosenOptions)
                ((ModuleXOption) c).remainingChoices--;
            addToBot(new ChooseOneAction(unchosenOptions));
        }
    }

    @Override
    public void upgrade() {
    }

    @Override
    public AbstractCard makeCopy() {
        return null;
    }
}
