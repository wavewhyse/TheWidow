package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;

import java.util.ArrayList;

import static theWidow.WidowMod.makeCardPath;

public class Meltdown extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Meltdown.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final UIStrings uistrings = CardCrawlGame.languagePack.getUIString(WidowMod.makeID(MeltdownAction.class.getSimpleName()));
    public static final String IMG = makeCardPath("Meltdown.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;

    private static final int COPIES = 2;
    private static final int UPGRADE_PLUS_COPIES = 1;

    // /STAT DECLARATION/

    public Meltdown() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = COPIES;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new MeltdownAction());
    }

    class MeltdownAction extends AbstractGameAction {
        private AbstractPlayer p = AbstractDungeon.player;
        private ArrayList<AbstractCard> unupgraded = new ArrayList<>();
        private final float DURATION = Settings.ACTION_DUR_FAST;
        public MeltdownAction() {
            actionType = ActionType.CARD_MANIPULATION;
            duration = DURATION;
        }
        @Override
        public void update() {
            if (duration == DURATION) {
                for (AbstractCard c : p.hand.group) {
                    if (!c.upgraded || c.tags.contains(AbstractCard.CardTags.HEALING))
                        unupgraded.add(c);
                }
                if (unupgraded.size() == p.hand.size()) {
                    isDone = true;
                    return;
                }

                if (p.hand.size() - unupgraded.size() == 1) {
                    for (AbstractCard c : p.hand.group) {
                        if (c.upgraded) {
                            doMeltdown(c);
                            isDone = true;
                            return;
                        }
                    }
                }
                p.hand.group.removeAll(unupgraded);
                if (p.hand.size() > 1) {
                    AbstractDungeon.handCardSelectScreen.open(uistrings.TEXT[0], 1, false, false, false, false);
                    tickDuration();
                    return;
                }
                if (p.hand.size() == 1) {
                    doMeltdown(p.hand.getTopCard());
                    returnCards();
                    isDone = true;
                }
            }
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    doMeltdown(c);
                }
                returnCards();
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                isDone = true;
            }
            tickDuration();
        }
        private void doMeltdown(AbstractCard c) {
            for (int i = 0; i < magicNumber; i++)
                addToTop(new MakeTempCardInHandAction(c.makeCopy()));
            p.hand.moveToExhaustPile(c);
        }
        private void returnCards() {
            for (AbstractCard c : this.unupgraded)
                p.hand.addToTop(c);
            p.hand.refreshHandLayout();
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_COPIES);
            initializeDescription();
        }
    }
}
