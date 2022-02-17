package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theWidow.WidowMod;

import java.util.ArrayList;

import static theWidow.WidowMod.makeCardPath;

public class ParticleAccelerator extends CustomCard {

    public static final String ID = WidowMod.makeID(ParticleAccelerator.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(WidowMod.makeID(ParticleAcceleratorAction.class.getSimpleName()));
    public static final String IMG = makeCardPath("DischargeBattery.png");// "public static final String IMG = makeCardPath("ParticleAccelerator.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = theWidow.TheWidow.Enums.COLOR_BLACK;

    private static final int COST = -1;

    public ParticleAccelerator() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ParticleAcceleratorAction());
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }

    private static final float DURATION = Settings.ACTION_DUR_MED;
    public class ParticleAcceleratorAction extends AbstractGameAction {
        private final AbstractPlayer p = AbstractDungeon.player;
        private final ArrayList<AbstractCard> costedCards = new ArrayList<>();
        public ParticleAcceleratorAction() {
            actionType = ActionType.CARD_MANIPULATION;
            duration = DURATION;
        }
        @Override
        public void update() {
            if (duration == DURATION) {
                if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                    isDone = true;
                    return;
                }
                for (AbstractCard c: p.hand.group)
                    if (c.costForTurn != 0)
                        costedCards.add(c);
                if (costedCards.size() >= p.hand.size()) {
                    isDone = true;
                    return;
                }
                if (p.hand.size() - costedCards.size() == 1) {
                    for (AbstractCard c: p.hand.group)
                        if (c.costForTurn == 0)
                            addToTop(new AbstractGameAction() {
                                @Override
                                public void update() {
                                    playTheCard(c);
                                    isDone = true;
                                }
                            });
                    isDone = true;
                    return;
                }

                p.hand.group.removeAll(costedCards);
                if (p.hand.size() == 1) {
                    playTheCard(p.hand.getTopCard());
                    returnCards();
                    isDone = true;
                } else {
                    AbstractDungeon.handCardSelectScreen.open(uiStrings.TEXT[0], 1, false);
                    tickDuration();
                    return;
                }
            }
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                for (AbstractCard c: AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    playTheCard(c);
                }
                returnCards();
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                isDone = true;
            }
            tickDuration();
        }

        private void playTheCard(AbstractCard c) {
            int effect = EnergyPanel.totalCount;
            if (energyOnUse != -1)
                effect = energyOnUse;
            if (p.hasRelic(ChemicalX.ID)) {
                effect += ChemicalX.BOOST;
                p.getRelic(ChemicalX.ID).flash();
            }
            if (upgraded)
                effect++;
            p.hand.group.remove(c);
            AbstractDungeon.getCurrRoom().souls.remove(c);
            p.limbo.group.add(c);
            c.current_y = -200.0F * Settings.scale;
            c.target_x = (float)Settings.WIDTH / 2.0F + 200.0F * Settings.xScale;
            c.target_y = (float)Settings.HEIGHT / 2.0F;
            c.targetAngle = 0.0F;
            c.lighten(false);
            c.drawScale = 0.12F;
            c.targetDrawScale = 0.75F;
            c.applyPowers();
            for (int i = 1; i < effect; i++) {
//                AbstractCard tmp = c.makeSameInstanceOf();
//                p.limbo.addToBottom(tmp);
//                tmp.current_x = c.current_x;
//                tmp.current_y = c.current_y;
//                tmp.target_x = (float)Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
//                tmp.target_y = (float)Settings.HEIGHT / 2.0F;
//                AbstractMonster m = null;
//                if (tmp.target == CardTarget.ENEMY) {
//                    m = AbstractDungeon.getRandomMonster();
//                    tmp.calculateCardDamage(m);
//                }
//                tmp.purgeOnUse = true;
//                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, c.energyOnUse, true, true), true);
                GameActionManager.queueExtraCard(c, AbstractDungeon.getRandomMonster());
            }
            AbstractCreature target = null;
            if (c.target == CardTarget.ENEMY || c.target == CardTarget.SELF_AND_ENEMY)
                target = AbstractDungeon.getRandomMonster();
            this.addToTop(new NewQueueCardAction(c, target, false, true));
            this.addToTop(new UnlimboAction(c));
            if (!Settings.FAST_MODE)
                addToTop(new WaitAction(Settings.ACTION_DUR_MED));
            else
                addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
            if (!freeToPlayOnce)
                p.energy.use(EnergyPanel.totalCount);
            isDone = true;
        }

        private void returnCards() {
            for (AbstractCard c : costedCards)
                p.hand.addToTop(c);
            p.hand.refreshHandLayout();
        }
    }
}
