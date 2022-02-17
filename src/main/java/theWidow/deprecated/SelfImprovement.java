package theWidow.deprecated;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
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

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
@Deprecated
public class SelfImprovement extends CustomCard {

    public static final String ID = WidowMod.makeID(SelfImprovement.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final UIStrings uistrings = CardCrawlGame.languagePack.getUIString(WidowMod.makeID(SelfImprovementAction.class.getSimpleName()));
    public static final String IMG = makeCardPath("Attack.png");// "public static final String IMG = makeCardPath("SelfImprovement.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 0;
    private static final int DRAW_PER_UPGRADE = 1;
    private static final int UPGRADE_DRAW_PER_UPGRADE = 1;

    public SelfImprovement() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DRAW_PER_UPGRADE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelfImprovementAction());
    }

    class SelfImprovementAction extends AbstractGameAction {
        private final AbstractPlayer p = AbstractDungeon.player;
        private final float DURATION = Settings.ACTION_DUR_FAST;
        private int draws;
        public SelfImprovementAction() {
            actionType = ActionType.CARD_MANIPULATION;
            duration = DURATION;
            this.draws = draws;
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
                addToBot(new DrawCardAction(upgrades * magicNumber));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DRAW_PER_UPGRADE);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
