package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;

import java.util.ArrayList;
import java.util.ListIterator;

import static theWidow.WidowMod.makeCardPath;

public class Salvage extends CustomCard {

    public static final String ID = WidowMod.makeID(Salvage.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("JunkHeap.png");// "public static final String IMG = makeCardPath("Salvage.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int BLOCK = 6;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int DISCARDS = 4;
    private static final int UPGRADE_PLUS_DISCARDS = 2;

    public Salvage() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DISCARDS;
        baseBlock = BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        addToBot(new SalvageAction());
    }

    class SalvageAction extends AbstractGameAction {
        @Override
        public void update() {
            ArrayList<AbstractCard> cardsToDiscard = new ArrayList<>();
            ListIterator<AbstractCard> iterator = AbstractDungeon.player.drawPile.group.listIterator(AbstractDungeon.player.drawPile.size());
            while (iterator.hasPrevious()) {
                AbstractCard c = iterator.previous();
                if (!c.upgraded) {
                    cardsToDiscard.add(c);
                    if (cardsToDiscard.size() >= magicNumber)
                        break;
                }
            }
            for (AbstractCard c : cardsToDiscard)
                AbstractDungeon.player.drawPile.moveToDiscardPile(c);
            isDone = true;
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_DISCARDS);
        }
    }
}
