package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.potions.DistilledCardPotion;

import java.util.ArrayList;

import static theWidow.WidowMod.makeCardPath;

public class Distill extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Distill.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final UIStrings uistrings = CardCrawlGame.languagePack.getUIString(WidowMod.makeID(DistillAction.class.getSimpleName()));
    public static final String IMG = makeCardPath("Distill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = -1;

    // /STAT DECLARATION/

    public Distill() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DistillAction());
    }

    class DistillAction extends AbstractGameAction {
        private AbstractPlayer p = AbstractDungeon.player;
        private final float DURATION = Settings.ACTION_DUR_FAST;
        private ArrayList<AbstractCard> unpotionable = new ArrayList<>();
        public DistillAction() {
            actionType = ActionType.CARD_MANIPULATION;
            duration = DURATION;
        }
        @Override
        public void update() {
            if (duration == DURATION) {
                int effect = EnergyPanel.totalCount;
                if (energyOnUse != -1)
                    effect = energyOnUse;
                if (p.hasRelic(ChemicalX.ID)) {
                    effect += ChemicalX.BOOST;
                    p.getRelic(ChemicalX.ID).flash();
                }
                if(upgraded)
                    effect++;

                for (AbstractCard c : p.hand.group) {
                    if (c.costForTurn > effect || c.type == AbstractCard.CardType.STATUS || c.type == AbstractCard.CardType.CURSE)
                        unpotionable.add(c);
                }
                if (unpotionable.size() == p.hand.size()) {
                    if (!freeToPlayOnce)
                        p.energy.use(EnergyPanel.totalCount);
                    isDone = true;
                    return;
                }
                if (p.hand.size() - unpotionable.size() == 1) {
                    AbstractCard c = null;
                    for (AbstractCard hc : p.hand.group)
                        if (!unpotionable.contains(hc))
                            c = hc;
                    doDistill(c);
                    //p.hand.group.stream().filter(c -> !unpotionable.contains(c)).forEach(this::doDistill);
                    return;
                }

                p.hand.group.removeAll(unpotionable);
                AbstractDungeon.handCardSelectScreen.open(uistrings.TEXT[0], 1, false, false, false, false);
                tickDuration();
                return;
            }
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                doDistill(AbstractDungeon.handCardSelectScreen.selectedCards.getTopCard());
                returnCards();
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            }
            tickDuration();
        }
        private void doDistill(AbstractCard c) {
            AbstractCard cc = c.makeSameInstanceOf();
            cc.energyOnUse = energyOnUse;
            cc.freeToPlayOnce = true;
            cc.purgeOnUse = true;
            addToBot(new ObtainPotionAction(new DistilledCardPotion(cc)));
            p.hand.moveToExhaustPile(c);
            if (!freeToPlayOnce)
                p.energy.use(EnergyPanel.totalCount);
            isDone = true;
        }
        private void returnCards() {
            for (AbstractCard c : unpotionable)
                p.hand.addToTop(c);
            p.hand.refreshHandLayout();
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
