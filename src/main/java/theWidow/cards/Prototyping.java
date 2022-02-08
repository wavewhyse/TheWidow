package theWidow.cards;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.WidowDowngradeCardAction;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;

public class Prototyping extends CustomCard {

    public static final String ID = WidowMod.makeID(Prototyping.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");// "public static final String IMG = makeCardPath("Prototyping.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;

    public Prototyping() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        if (CardLibrary.getAllCards() != null && !CardLibrary.getAllCards().isEmpty())
            theWidow.util.artHelp.CardArtRoller.computeCard(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = p.hand.size() - 1; i < BaseMod.MAX_HAND_SIZE; i++) {
            addToBot(new DrawCardAction(1));
            addToBot(new PrototypingDowngradeAction());
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }

    private static final float DURATION = Settings.ACTION_DUR_XFAST;
    public class PrototypingDowngradeAction extends AbstractGameAction {
        public PrototypingDowngradeAction() {
            actionType = ActionType.CARD_MANIPULATION;
            duration = DURATION;
        }
        @Override
        public void update() {
            if (AbstractDungeon.player.hand.getTopCard().upgraded)
                addToTop(new WidowDowngradeCardAction(AbstractDungeon.player.hand.getTopCard()));
            isDone = true;
        }
    }
}
