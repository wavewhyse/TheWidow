package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import theWidow.TheWidow;
import theWidow.WidowMod;

import static theWidow.WidowMod.makeCardPath;

public class Unstoppable extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Unstoppable.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Attack.png");// "public static final String IMG = makeCardPath("Unstoppable.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 4;
    public static final int SCALING = 2;
    public static final int UPGRADE_PLUS_SCALING = 1;

    // /STAT DECLARATION/

    public Unstoppable() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = SCALING;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        rawDescription = cardStrings.DESCRIPTION;
        rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int counter = 0;
        for (AbstractPower pow : p.powers) {
            switch (pow.ID) {
                /*case StrengthPower.POWER_ID:
                case DexterityPower.POWER_ID:
                    if (pow.amount < 0){
                        powersToRemove.add(pow);
                    }
                    break;
                case EnergyDownPower.POWER_ID:
                case DrawReductionPower.POWER_ID:*/
                case VulnerablePower.POWER_ID:
                case WeakPower.POWER_ID:
                case FrailPower.POWER_ID:
                    counter += pow.amount;
                    addToBot(new RemoveSpecificPowerAction(p, p, pow));
            }
        }
        int finalCounter = counter;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int oldBaseDamage = baseDamage;
                baseDamage += finalCounter * magicNumber;
                calculateCardDamage(m);
                addToTop( new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                baseDamage = oldBaseDamage;
                isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_SCALING);
            initializeDescription();
        }
    }
}
