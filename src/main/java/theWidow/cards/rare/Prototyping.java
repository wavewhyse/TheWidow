package theWidow.cards.rare;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.DoToNewCardAction;
import theWidow.actions.WidowDowngradeCardAction;

import static theWidow.WidowMod.makeCardPath;

public class Prototyping extends CustomCard {
    public static final String ID = WidowMod.makeID(Prototyping.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Prototyping() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Prototyping.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.RARE,
                CardTarget.SELF );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = p.hand.size() - 1; i < BaseMod.MAX_HAND_SIZE; i++) {
            addToBot(new DrawCardAction(1));
            addToBot(new DoToNewCardAction((card, params) -> {
                if (card.upgraded)
                    addToTop(new WidowDowngradeCardAction(card));
                return true;
            }));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
            initializeDescription();
        }
    }
}
