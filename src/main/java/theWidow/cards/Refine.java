package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeCardAction;

import static theWidow.WidowMod.makeCardPath;

public class Refine extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Refine.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("Refine.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;

    // /STAT DECLARATION/

    public Refine() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        class RefineAction extends AbstractGameAction {
            private CardGroup upgradeable = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            private final float DURATION = Settings.ACTION_DUR_FAST;
            public RefineAction() {
                actionType = ActionType.CARD_MANIPULATION;
                duration = DURATION;
            }
            @Override
            public void update() {
                if (duration == DURATION) {
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
                        AbstractDungeon.gridSelectScreen.open(upgradeable, 1, "Refine", true, false, false, false);
                        tickDuration();
                        return;
                    }
                }
                if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                    refineCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0), true);
                    /*for (AbstractCard c : upgradeable.group) {
                        c.unhover();
                        c.target_x = CardGroup.DISCARD_PILE_X;
                        c.target_y = 0.0F;
                    }*/
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
                c.lighten(false);
                if (unhover)
                    c.unhover();
                c.applyPowers();
            }
        }
        addToBot(new RefineAction());
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            exhaust = false;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
