package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.Wiz;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static theWidow.WidowMod.makeCardPath;

public class Salvage extends CustomCard {
    public static final String ID = WidowMod.makeID(Salvage.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Salvage() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Salvage.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.SELF );
        magicNumber = baseMagicNumber = 4;
        baseBlock = 9;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        addToBot(new SalvageAction());
    }

    class SalvageAction extends AbstractGameAction {
        @Override
        public void update() {
            List<AbstractCard> cardsToDiscard = new ArrayList<>();
            ListIterator<AbstractCard> iterator = Wiz.adp().drawPile.group.listIterator(Wiz.adp().drawPile.size());
            while (iterator.hasPrevious()) {
                AbstractCard c = iterator.previous();
                if (!c.upgraded) {
                    cardsToDiscard.add(c);
                    if (cardsToDiscard.size() >= magicNumber)
                        break;
                }
            }
            for (AbstractCard c : cardsToDiscard)
                Wiz.adp().drawPile.moveToDiscardPile(c);
            isDone = true;
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(3);
            upgradeMagicNumber(2);
        }
    }
}
