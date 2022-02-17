package theWidow.deprecated;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.vfx.UpgradeHammerHit;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
@Deprecated
public class Inspiration extends CustomCard {

    public static final String ID = WidowMod.makeID(Inspiration.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("Attack.png");// "public static final String IMG = makeCardPath("Inspiration.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = -1;

    public Inspiration() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        class InspirationAction extends AbstractGameAction {
            private final float DURATION = Settings.ACTION_DUR_LONG;
            private final ArrayList<AbstractCard> cannotUpgrade = new ArrayList<>();
            private int effect;
            public InspirationAction() {
                actionType = ActionType.CARD_MANIPULATION;
                duration = DURATION;
            }
            @Override
            public void update() {
                if (duration == DURATION) {
                    effect = EnergyPanel.totalCount;
                    if (energyOnUse != -1)
                        effect = energyOnUse;
                    if (p.hasRelic(ChemicalX.ID)) {
                        effect += ChemicalX.BOOST;
                        p.getRelic(ChemicalX.ID).flash();
                    }
                    if(upgraded)
                        effect++;
                    if (effect == 0) {
                        if (!freeToPlayOnce)
                            p.energy.use(EnergyPanel.totalCount);
                        isDone = true;
                        return;
                    }

                    for (AbstractCard c : p.hand.group) {
                        if (!c.canUpgrade())
                            cannotUpgrade.add(c);
                    }
                    if (cannotUpgrade.size() == p.hand.size()) {
                        if (!freeToPlayOnce)
                            p.energy.use(EnergyPanel.totalCount);
                        isDone = true;
                        return;
                    }
                    if (p.hand.size() - cannotUpgrade.size() <= 1) {
                        for (AbstractCard c : p.hand.group) {
                            doUpgrades(c);
                        }
                        if (!freeToPlayOnce)
                            p.energy.use(EnergyPanel.totalCount);
                        isDone = true;
                        return;
                    }

                    p.hand.group.removeAll(cannotUpgrade);
                    if (p.hand.size() > 1) {
                        AbstractDungeon.handCardSelectScreen.open("upgrade", 1, false, false, false, true);
                        tickDuration();
                        return;
                    }
                    if (p.hand.size() <= 1) {
                        for (AbstractCard c : p.hand.group)
                            doUpgrades(c);
                        returnCards();
                        if (!freeToPlayOnce)
                            p.energy.use(EnergyPanel.totalCount);
                        isDone = true;
                    }
                }
                if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                    for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                        doUpgrades(c);
                        p.hand.addToTop(c);
                    }
                    returnCards();
                    AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                    AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                    if (!freeToPlayOnce)
                        p.energy.use(EnergyPanel.totalCount);
                    isDone = true;
                }
                tickDuration();
            }
            private void doUpgrades(AbstractCard c) {
                for (int i = 0; i < effect && c.canUpgrade(); i++) {
                    c.upgrade();
                    AbstractDungeon.effectsQueue.add(new UpgradeHammerHit(c));
                }
                c.costForTurn = 0;
                c.applyPowers();
            }
            private void returnCards() {
                for (AbstractCard c : cannotUpgrade)
                    p.hand.addToTop(c);
                p.hand.refreshHandLayout();
            }
        }
        addToBot(new InspirationAction());
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
