package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.WidowMod;
import theWidow.actions.WidowDowngradeCardAction;
import theWidow.actions.WidowUpgradeCardAction;
import theWidow.characters.TheWidow;

import static theWidow.WidowMod.makeCardPath;

public class Scrapskin extends CustomCard implements Downgradeable{

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Scrapskin.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("Skill.png");// "public static final String IMG = makeCardPath("Scrapskin.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int BLOCK = 5;

    // /STAT DECLARATION/

    public Scrapskin() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        if (upgraded) {
            addToBot(new RemoveDebuffsAction(p));
            addToBot(new WidowDowngradeCardAction(this, true));
            /*addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    for (AbstractCard c : p.masterDeck.group) {
                        if (c.uuid.equals(Scrapskin.this.uuid) && c.upgraded) {
                            ((Scrapskin)c).downgrade();
                            AbstractCard display = c.makeStatEquivalentCopy();
                            AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(display));
                            AbstractDungeon.effectList.add(new ExhaustCardEffect(display));
                            break;
                        }
                    }
                    downgrade();
                    superFlash();
                    isDone = true;
                }
            });*/
        } else {
            addToBot(new WidowUpgradeCardAction(this, true));
            /*for (AbstractCard c : p.masterDeck.group) {
                if (c.uuid.equals(this.uuid) && !c.upgraded) {
                    c.upgrade();
                    AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                    addToTop(new WaitAction(Settings.ACTION_DUR_MED));
                }
            }
            upgrade();*/
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            rawDescription = UPGRADE_DESCRIPTION;
            cardsToPreview = makeCopy();
            upgradeName();
            initializeDescription();
        }
    }

    @Override
    public void downgrade() {
        upgraded = false;
        timesUpgraded--;
        name = cardStrings.NAME;
        rawDescription = cardStrings.DESCRIPTION;
        cardsToPreview = null;
        initializeTitle();
        initializeDescription();
    }
}
