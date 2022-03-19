package theWidow.cards.uncommon;

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
import theWidow.actions.WidowUpgradeCardAction;

import static theWidow.WidowMod.makeCardPath;

public class Weaving extends CustomCard {
    public static final String ID = WidowMod.makeID(Weaving.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Weaving() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Weaving.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.NONE );
        magicNumber = baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new DrawCardAction(1));
            if (p.hand.size() + i <= BaseMod.MAX_HAND_SIZE)
                addToBot(new DoToNewCardAction((card, params) -> {
                    addToTop(new WidowUpgradeCardAction(card));
                    return true;
                }));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            initializeDescription();
        }
    }

}
