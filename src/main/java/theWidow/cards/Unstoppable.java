package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
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

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 3;
    public static final int SCALING = 2;
    public static final int UPGRADE_PLUS_SCALING = 1;

    // /STAT DECLARATION/

    public Unstoppable() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = SCALING;
//        exhaust = true;
        if (CardLibrary.getAllCards() != null && !CardLibrary.getAllCards().isEmpty())
            theWidow.util.artHelp.CardArtRoller.computeCard(this);
    }

//    @Override
//    public void applyPowers() {
//        super.applyPowers();
//        rawDescription = cardStrings.DESCRIPTION;
//        rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
//        initializeDescription();
//    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new InflameEffect(p)));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        addToBot(new RemoveSpecificPowerAction(p, p, VulnerablePower.POWER_ID));
        addToBot(new RemoveSpecificPowerAction(p, p, WeakPower.POWER_ID));
        addToBot(new RemoveSpecificPowerAction(p, p, FrailPower.POWER_ID));
//        addToBot(new AbstractGameAction() {
//            @Override
//            public void update() {
//                ArrayList<AbstractPower> powersToRemove = new ArrayList<>();
//                for (AbstractPower pow : p.powers) {
//                    switch (pow.ID) {
//                        case VulnerablePower.POWER_ID:
//                        case WeakPower.POWER_ID:
//                        case FrailPower.POWER_ID:
//                            powersToRemove.add(pow);
//                    }
//                }
//                addToTop(new AbstractGameAction() {
//                    @Override
//                    public void update() {
//                        int oldBaseDamage = baseDamage;
//                        baseDamage += powersToRemove.size() * magicNumber;
//                        calculateCardDamage(m);
//                        addToTop(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
//                        baseDamage = oldBaseDamage;
//                        calculateCardDamage(m);
//                        isDone = true;
//                    }
//                });
//                for (AbstractPower pow : powersToRemove)
//                    addToTop(new RemoveSpecificPowerAction(p, p, pow));
//                isDone = true;
//            }
//        });
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
