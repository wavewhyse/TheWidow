package theWidow.cards.rare;

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
import theWidow.util.Wiz;

import java.util.ArrayList;
import java.util.List;

import static theWidow.WidowMod.makeCardPath;

public class Distill extends CustomCard {
    public static final String ID = WidowMod.makeID(Distill.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final UIStrings uistrings = CardCrawlGame.languagePack.getUIString(WidowMod.makeID(DistillAction.class.getSimpleName()));

    public Distill() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Distill.class.getSimpleName()),
                -1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.RARE,
                CardTarget.NONE );
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DistillAction());
    }

    private class DistillAction extends AbstractGameAction {
        private final List<AbstractCard> unpotionable = new ArrayList<>();

        public DistillAction() {
            actionType = ActionType.CARD_MANIPULATION;
            duration = Settings.ACTION_DUR_FAST;
        }

        @Override
        public void update() {
            if (duration == Settings.ACTION_DUR_FAST) {
                int effect = EnergyPanel.totalCount;
                if (energyOnUse != -1)
                    effect = energyOnUse;
                if (Wiz.adp().hasRelic(ChemicalX.ID)) {
                    effect += ChemicalX.BOOST;
                    Wiz.adp().getRelic(ChemicalX.ID).flash();
                }
                if(upgraded)
                    effect++;

                for (AbstractCard c : Wiz.adp().hand.group) {
                    if (c.costForTurn > effect || c.type == AbstractCard.CardType.STATUS || c.type == AbstractCard.CardType.CURSE)
                        unpotionable.add(c);
                }
                if (unpotionable.size() == Wiz.adp().hand.size()) {
                    if (!freeToPlayOnce)
                        Wiz.adp().energy.use(EnergyPanel.totalCount);
                    isDone = true;
                    return;
                }
                if (Wiz.adp().hand.size() - unpotionable.size() == 1) {
                    AbstractCard c = null;
                    for (AbstractCard hc : Wiz.adp().hand.group)
                        if (!unpotionable.contains(hc))
                            c = hc;
                    doDistill(c);
                    return;
                }

                Wiz.adp().hand.group.removeAll(unpotionable);
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
            Wiz.adp().hand.moveToExhaustPile(c);
            if (!freeToPlayOnce)
                Wiz.adp().energy.use(EnergyPanel.totalCount);
            isDone = true;
        }

        private void returnCards() {
            for (AbstractCard c : unpotionable)
                Wiz.adp().hand.addToTop(c);
            Wiz.adp().hand.refreshHandLayout();
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
        }
    }
}
