package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.potions.GrenadePotion;
import theWidow.powers.ExtraCompartmentPower;

import static theWidow.WidowMod.makeCardPath;

public class ExtraCompartment extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(ExtraCompartment.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("ExtraCompartment.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int SLOTS = 1;
    private static final int EXHAUSTIVE = 2;
    private static final int UPGRADE_PLUS_EXHAUSTIVE = 1;

    // /STAT DECLARATION/

    public ExtraCompartment() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = SLOTS;
        ExhaustiveVariable.setBaseValue(this, EXHAUSTIVE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ExtraCompartmentPower(p, 1), 1));
        addToBot(new ObtainPotionAction(new GrenadePotion()));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            ExhaustiveVariable.upgrade(this, UPGRADE_PLUS_EXHAUSTIVE);
        }
    }
}
