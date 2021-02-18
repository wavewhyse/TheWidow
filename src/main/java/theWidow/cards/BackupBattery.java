package theWidow.cards;

import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.relics.SewingKitRelic;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;

public class BackupBattery extends BetaCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(BackupBattery.class.getSimpleName());
    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("BackupBattery.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 0;
    private static final int ENERGY_GAIN = 2;
    private static final int EXHAUSTIVE = 1;

    // /STAT DECLARATION/

    public BackupBattery() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        ExhaustiveVariable.setBaseValue(this, EXHAUSTIVE);
        magicNumber = baseMagicNumber = ENERGY_GAIN;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SewingKitRelic.ID))
            upgrade();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(magicNumber));
    }

    @Override
    public void upgrade() { //TODO: make the exhaustive increase by 1 instead of completely resetting
        upgradeName();
        ExhaustiveVariable.upgrade(this, 1);
    }
}
