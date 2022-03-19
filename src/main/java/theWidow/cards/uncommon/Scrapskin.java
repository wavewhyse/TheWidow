package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.WidowDowngradeCardAction;
import theWidow.actions.WidowUpgradeCardAction;
import theWidow.cards.Downgradeable;
import theWidow.util.CardArtRoller;

import static theWidow.WidowMod.makeCardPath;

public class Scrapskin extends CustomCard implements Downgradeable {
    public static final String ID = WidowMod.makeID(Scrapskin.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Scrapskin() {
        super( ID,
                cardStrings.NAME,
                makeCardPath("Skill"),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.SELF );
        baseBlock = 5;
        if (CardLibrary.getAllCards() != null && !CardLibrary.getAllCards().isEmpty())
            CardArtRoller.computeCard(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        if (upgraded) {
            addToBot(new RemoveDebuffsAction(p));
            addToBot(new WidowDowngradeCardAction(this, true));
        } else
            addToBot(new WidowUpgradeCardAction(this, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            cardsToPreview = makeCopy();
            upgradeName();
            initializeDescription();
        }
    }

    @Override
    public void downgrade() {
        upgraded = false;
        timesUpgraded--;
        name = cardStrings.NAME;
        rawDescription = cardStrings.DESCRIPTION;
        cardsToPreview = null;
        initializeTitle();
        initializeDescription();
    }
}
