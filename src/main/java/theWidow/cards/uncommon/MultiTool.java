package theWidow.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.DoToNewCardAction;
import theWidow.actions.WidowUpgradeCardAction;
import theWidow.cards.BetaCard;
import theWidow.util.CardArtRoller;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class MultiTool extends BetaCard {
    public static final String ID = WidowMod.makeID(MultiTool.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public MultiTool() {
        super( ID,
                cardStrings.NAME,
                makeCardPath("Skill"),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.NONE,
                cardStrings );
        magicNumber = baseMagicNumber = 0;
        exhaust = true;
        if (CardLibrary.getAllCards() != null && !CardLibrary.getAllCards().isEmpty())
            CardArtRoller.computeCard(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard card = Wiz.returnTrulyRandomPrediCardInCombat(c -> c instanceof BetaCard, true);
        card.setCostForTurn(0);
        addToBot(new MakeTempCardInHandAction(card));
        addToBot(new DoToNewCardAction((c, params) -> {
            for (int i = 0; i < (int) params[0]; i++)
                addToTop(new WidowUpgradeCardAction(c));
            return true;
        }, magicNumber));
    }

    @Override
    public void upgrade() {
        upgradeName();
        upgradeMagicNumber(2);
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
