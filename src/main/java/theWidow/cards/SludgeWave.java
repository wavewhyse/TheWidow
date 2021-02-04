package theWidow.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import theWidow.WidowMod;
import theWidow.characters.TheWidow;

import static theWidow.WidowMod.makeCardPath;

public class SludgeWave extends ExtraExtraMagicalCustomCard implements Downgradeable {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(SludgeWave.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("SludgeWave.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int WEAK = 2;
    private static final int UPGRADE_PLUS_WEAK = 1;
    //private static final int NECROSIS = 5;
    //private static final int UPGRADE_PLUS_NECROSIS = 2;
    //private static final int SELF_DEBUFFS = 2;
    //private static final int UPGRADE_PLUS_SELF_DEBUFFS = -1;

    // /STAT DECLARATION/

    public SludgeWave() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = WEAK;
        //secondMagicNumber = baseSecondMagicNumber = NECROSIS;
        //thirdMagicNumber = baseThirdMagicNumber = SELF_DEBUFFS;
        //exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters)
        //    addToBot( new ApplyPowerAction(mon, p, new NecrosisPower(mon, magicNumber), magicNumber));
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            addToBot(new ApplyPowerAction(mon, p, new WeakPower(mon, magicNumber, false), magicNumber));
            addToBot(new ApplyPowerAction(mon, p, new VulnerablePower(mon, magicNumber, false), magicNumber));
        }
        addToBot( new ApplyPowerAction(p, p, new WeakPower(p, magicNumber, false), magicNumber));
        addToBot( new ApplyPowerAction(p, p, new VulnerablePower(p, magicNumber, false), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_WEAK);
            //upgradeSecondMagicNumber(UPGRADE_PLUS_NECROSIS);
            //upgradeThirdMagicNumber(UPGRADE_PLUS_SELF_DEBUFFS);
            initializeDescription();
        }
    }

    @Override
    public void downgrade() {
        if (upgraded) {
            name = cardStrings.NAME;
            timesUpgraded--;
            upgraded = false;
            magicNumber = baseMagicNumber = WEAK;
            //secondMagicNumber = baseSecondMagicNumber = WEAK;
            //thirdMagicNumber = baseThirdMagicNumber = SELF_DEBUFFS;
            upgradedMagicNumber = upgradedSecondMagicNumber = upgradedThirdMagicNumber = false;
        }
    }
}
