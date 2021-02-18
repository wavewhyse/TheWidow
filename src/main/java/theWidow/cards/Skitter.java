package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.WebPower;

import static theWidow.WidowMod.makeCardPath;

public class Skitter extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Skitter.class.getSimpleName());
    public static final String IMG = makeCardPath("Skitter.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private static final int WEB = 1;

    // /STAT DECLARATION/

    public Skitter() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = WEB;
        exhaust = true;
        isInnate = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new WebPower(p,
                magicNumber * AbstractDungeon.getMonsters().monsters.size()),
                magicNumber * AbstractDungeon.getMonsters().monsters.size()));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}
