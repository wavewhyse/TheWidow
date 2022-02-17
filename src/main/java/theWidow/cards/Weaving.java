package theWidow.cards;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeCardAction;

import static theWidow.WidowMod.makeCardPath;

public class Weaving extends CustomCard {

    public static final String ID = WidowMod.makeID(Weaving.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("Weaving.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DRAW = 2;
    private static final int UPGRADE_PLUS_DRAW = 1;

    public Weaving() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new DrawCardAction(1));
            if (p.hand.size() + i < BaseMod.MAX_HAND_SIZE)
                addToBot(new WeavingUpgradeAction());
        }
//        boolean upgradeFirst = false;
//        if (!upgraded)
//            upgradeFirst = AbstractDungeon.cardRandomRng.randomBoolean();
//        addToBot(new DrawCardAction(1));
//        if (p.hand.size() < BaseMod.MAX_HAND_SIZE && (upgraded || upgradeFirst))
//            addToBot(new WeavingUpgradeAction());
//        addToBot(new DrawCardAction(1));
//        if (p.hand.size() + 1 < BaseMod.MAX_HAND_SIZE && (upgraded || !upgradeFirst))
//            addToBot(new WeavingUpgradeAction());
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            initializeDescription();
        }
    }

    private static final float DURATION = Settings.ACTION_DUR_XFAST;
    public static class WeavingUpgradeAction extends AbstractGameAction {
        public WeavingUpgradeAction() {
            actionType = ActionType.CARD_MANIPULATION;
            duration = DURATION;
        }
        @Override
        public void update() {
            if (AbstractDungeon.player.hand.getTopCard().canUpgrade())
                addToTop(new WidowUpgradeCardAction(AbstractDungeon.player.hand.getTopCard()));
            isDone = true;
        }
    }
}
