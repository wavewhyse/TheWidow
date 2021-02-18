package theWidow.cards;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
public class MultiTool extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(MultiTool.class.getSimpleName());
    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("Skill.png");// "public static final String IMG = makeCardPath("MultiTool.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int UPGRADES = 2;
    private static final int UPGRADE_PLUS_UPGRADES = 1;
    private static final int EXHAUSTIVE = 3;

    // /STAT DECLARATION/

    public MultiTool() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = UPGRADES;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                ArrayList<AbstractCard> cannotUpgrade = new ArrayList<>();
                if (duration == Settings.ACTION_DUR_FAST) {
                    if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                        isDone = true;
                        return;
                    }
                    for (AbstractCard c : p.hand.group) {
                        if (!c.canUpgrade())
                            cannotUpgrade.add(c);
                    }
                    if (cannotUpgrade.size() == p.hand.size()) {
                        isDone = true;
                        return;
                    }
                    if (p.hand.size() - cannotUpgrade.size() <= amount) {
                        for (AbstractCard c : p.hand.group) {
                            if (c.canUpgrade());
                                //addToTop(new W);
                        }
                        isDone = true;
                        return;
                    }
                    p.hand.group.removeAll(cannotUpgrade);
                    if (p.hand.size() <= amount) {
                        for (AbstractCard c : p.hand.group);
                            //addToTop(new WidowUpgradeManagerAction.UpgradeOneCardAction(c));
                        //returnCards();
                        isDone = true;
                    }
                    if (p.hand.size() > amount) {
                        AbstractDungeon.handCardSelectScreen.open("Upgrade", amount, false, false, false, true);
                        tickDuration();
                        return;
                    }
                }
                if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                    for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                        //addToTop(new WidowUpgradeManagerAction.UpgradeOneCardAction(c));
                        p.hand.addToTop(c);
                    }
                    //returnCards();
                    AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                    AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                    isDone = true;
                }
                tickDuration();
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_UPGRADES);
        }
    }
}
