package theWidow.cards.ModuleXOptions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.BattleHymnPower;
import theWidow.WidowMod;

import java.util.ArrayList;

import static theWidow.WidowMod.makeCardPath;

public class Resonate extends ModuleXOption {
    public static final String ID = WidowMod.makeID(Resonate.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Resonate() {
        this(new ArrayList<>(), 0);
    }

    public Resonate(ArrayList<AbstractCard> unchosenOptions, int remainingChoices) {
        super(unchosenOptions, remainingChoices,
                ID,
                cardStrings.NAME,
                makeCardPath("Power.png"),
                cardStrings.DESCRIPTION
        );
        magicNumber = baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BattleHymnPower(p, 1), 1));
    }
}
