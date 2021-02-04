package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.WidowMod;
import theWidow.characters.TheWidow;

import static theWidow.WidowMod.makeCardPath;

public class SelfImprovement extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(SelfImprovement.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");// "public static final String IMG = makeCardPath("SelfImprovement.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int  UPGRADED_COST = 0;
    private static final int STATS = 1;

    // /STAT DECLARATION/

    public SelfImprovement() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = STATS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        class SelfImprovementAction extends AbstractGameAction {
            private final float DURATION = Settings.ACTION_DUR_FAST;
            public SelfImprovementAction() {
                actionType = ActionType.CARD_MANIPULATION;
                duration = DURATION;
            }
            @Override
            public void update() {
                if (duration == DURATION) {
                    if (p.hand.size() == 0) {
                        isDone = true;
                        return;
                    }
                    if (p.hand.size() == 1) {
                        eatCard(p.hand.getTopCard());
                        isDone = true;
                        return;
                    }
                    if (p.hand.size() > 1) {
                        AbstractDungeon.handCardSelectScreen.open("Exhaust", 1, false, false, false, false);
                        tickDuration();
                        return;
                    }
                }
                if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                    eatCard(AbstractDungeon.handCardSelectScreen.selectedCards.getTopCard());
                    AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                    AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                    isDone = true;
                }
                tickDuration();
            }
            private void eatCard(AbstractCard c) {
                int upgrades = c.timesUpgraded;
                p.hand.moveToExhaustPile(c);
                if (upgrades > 0) {
                    addToBot(new GainEnergyAction(upgrades));
                    addToBot(new DrawCardAction(upgrades));
                }
            }
        }
        addToBot(new SelfImprovementAction());
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}
