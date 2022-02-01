package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.potions.GrenadePotion;
import theWidow.powers.GrenadierPower;

import static theWidow.WidowMod.makeCardPath;

public class Anarchy extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Anarchy.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Grenadier.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int SLOTS = 1;
    private static final int UPGRADE_PLUS_SLOTS = 1;

    // /STAT DECLARATION/

    public Anarchy() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = SLOTS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToTop(new ApplyPowerAction(p, p, new GrenadierPower(p, magicNumber)));
        for (int i = 0; i < p.potionSlots + magicNumber - p.potions.size(); i++)
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
