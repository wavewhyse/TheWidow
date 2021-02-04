package theWidow.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.WidowMod;
import theWidow.characters.TheWidow;
import theWidow.potions.GrenadePotion;
import theWidow.powers.GrenadierPower;

import static theWidow.WidowMod.makeCardPath;

public class Grenadier extends ExtraMagicalCustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Grenadier.class.getSimpleName());
    public static final String IMG = makeCardPath("Grenadier.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int SLOTS = 1;
    private static final int UPGRADE_PLUS_SLOTS = 1;
    private static final int GRENADES = 2;

    // /STAT DECLARATION/

    public Grenadier() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = SLOTS;
        secondMagicNumber = baseSecondMagicNumber = GRENADES;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToTop(new ApplyPowerAction(p, p, new GrenadierPower(p, magicNumber)));
        for (int i = 0; i < secondMagicNumber; i++)
            addToBot(new ObtainPotionAction(new GrenadePotion()));
        addToBot(new AbstractGameAction() {
            public void update() { p.adjustPotionPositions(); isDone = true; }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_SLOTS);
            initializeDescription();
        }
    }
}
