package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeCardAction;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class Refine extends CustomCard {
    public static final String ID = WidowMod.makeID(Refine.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public Refine() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Refine.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.SELF );
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RefineAction());
    }

    public static class RefineAction extends AbstractGameAction {
        private final CardGroup upgradeable = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        private AbstractPlayer p = Wiz.adp();
        public RefineAction() {
            actionType = ActionType.CARD_MANIPULATION;
            duration = startDuration = Settings.ACTION_DUR_FAST;
        }
        @Override
        public void update() {
            if (duration == startDuration) {
                if (p.discardPile.isEmpty() || p.hand.size() == 10) {
                    isDone = true;
                    return;
                }
                if (p.discardPile.size() == 1) {
                    if (p.discardPile.getTopCard().canUpgrade())
                        refineCard(p.discardPile.getTopCard(), false);
                    isDone = true;
                    return;
                }
                for (AbstractCard c : p.discardPile.group) {
                    if (c.canUpgrade())
                        upgradeable.addToTop(c);
                }
                if (upgradeable.isEmpty()) {
                    isDone = true;
                    return;
                }
                if (upgradeable.size() ==  1) {
                    refineCard(upgradeable.getTopCard(), false);
                    isDone = true;
                    return;
                }
                if (upgradeable.size() > 1) {
                    AbstractDungeon.gridSelectScreen.open(upgradeable, 1, uiStrings.TEXT[0], true, false, false, false);
                    tickDuration();
                    return;
                }
            }
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                refineCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0), true);
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }
            tickDuration();
            if (isDone)
                for (AbstractCard c : p.hand.group)
                    c.applyPowers();
        }
        private void refineCard(AbstractCard c, boolean unhover) {
            p.hand.addToHand(c);
            p.discardPile.removeCard(c);
            addToTop(new WidowUpgradeCardAction(c));
            addToTop(new WidowUpgradeCardAction(c));
            addToTop(new WaitAction(Settings.ACTION_DUR_MED));
            c.lighten(false);
            if (unhover)
                c.unhover();
            c.applyPowers();
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            exhaust = false;
            initializeDescription();
        }
    }
}
