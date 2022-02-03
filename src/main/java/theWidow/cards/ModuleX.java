package theWidow.cards;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Smite;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.cards.ModuleXOptions.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;

public class ModuleX extends BetaCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(ModuleX.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = makeCardPath("Power.png");// "public static final String IMG = makeCardPath("ModuleXOptions.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    public static final int MAX_UPGRADES = 12;

    private enum Options {
        INFLAME, FOOTWORK, METALLICIZE, MACHINE_LEARNING, WELLLAID_PLANS, CALTROPS, BATTLE_HYMN, COST

    }
    private Random randomizer;
    private boolean[] upgradesTaken;
    protected Set<Options> availableOptions;

    // /STAT DECLARATION/

    public ModuleX() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        availableOptions = new LinkedHashSet<>();
        availableOptions.add(Options.INFLAME);
        availableOptions.add(Options.FOOTWORK);
        if (Settings.seed == null)
            randomizer = new Random();
        else
            randomizer = new Random(Settings.seed);
        upgradesTaken = new boolean[MAX_UPGRADES];
        magicNumber = baseMagicNumber = 1;
        rawDescription = getDescription();
        if (CardLibrary.getAllCards() != null && !CardLibrary.getAllCards().isEmpty())
            theWidow.util.artHelp.CardArtRoller.computeCard(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> unchosenOptions = new ArrayList<>();
        for (Options o: availableOptions)
            switch (o) {
                case INFLAME:
                    unchosenOptions.add(new ExhaustFlame(unchosenOptions, magicNumber - 1));
                    break;
                case FOOTWORK:
                    unchosenOptions.add(new Coordinate(unchosenOptions, magicNumber - 1));
                    break;
                case METALLICIZE:
                    unchosenOptions.add(new Alloy(unchosenOptions, magicNumber - 1));
                    break;
                case MACHINE_LEARNING:
                    unchosenOptions.add(new OnboardAI(unchosenOptions, magicNumber - 1));
                    break;
                case WELLLAID_PLANS:
                    unchosenOptions.add(new Precalculate(unchosenOptions, magicNumber - 1));
                    break;
                case CALTROPS:
                    unchosenOptions.add(new ShedShrapnel(unchosenOptions, magicNumber - 1));
                    break;
                case BATTLE_HYMN:
                    unchosenOptions.add(new Resonate(unchosenOptions, magicNumber - 1));
                    break;
            }
        if (magicNumber < unchosenOptions.size())
            addToBot(new ChooseOneAction(unchosenOptions));
        else    //TODO: COOL EFFECT IF THE CARD IS FULLY UPGRADED
            for (AbstractCard c:unchosenOptions)
                c.use(p, m);
    }

    @Override
    public boolean canUpgrade() {
        return timesUpgraded < MAX_UPGRADES;
    }

    @Override
    public void upgrade() {
        if (timesUpgraded < MAX_UPGRADES) {
            int thisUpgrade;
            do
                thisUpgrade = randomizer.random(0, MAX_UPGRADES-1);
            while (upgradesTaken[thisUpgrade] || ( thisUpgrade >= 6 && magicNumber >= availableOptions.size() ) );
            upgradesTaken[thisUpgrade] = true;

            switch (thisUpgrade) {
                case 0:
                    upgradeBaseCost(UPGRADED_COST);
                    break;
                case 1:
                    availableOptions.add(Options.METALLICIZE);
                    break;
                case 2:
                    availableOptions.add(Options.MACHINE_LEARNING);
                    break;
                case 3:
                    availableOptions.add(Options.WELLLAID_PLANS);
                    break;
                case 4:
                    availableOptions.add(Options.CALTROPS);
                    break;
                case 5:
                    availableOptions.add(Options.BATTLE_HYMN);
                    cardsToPreview = new Smite();
                    break;
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                default:
                    upgradeMagicNumber(1);
            }

            upgradeName();
            rawDescription = getDescription();
            initializeDescription();
        }
    }

    @Override
    public void downgrade() {
        super.downgrade();
        ModuleX downgradedVersion = new ModuleX();
        for (int i = 0; i<timesUpgraded; i++)
            downgradedVersion.upgrade();

        this.upgradesTaken = downgradedVersion.upgradesTaken;
        magicNumber = baseMagicNumber = downgradedVersion.baseMagicNumber;
        this.availableOptions = downgradedVersion.availableOptions;

        upgradeBaseCost(downgradedVersion.cost);
        this.upgradedCost = downgradedVersion.upgradedCost;

    }

    private String getDescription() {
        String desc = "";
        if (timesUpgraded < 12)
            desc += EXTENDED_DESCRIPTION[0];
        if (magicNumber < availableOptions.size())
            desc += EXTENDED_DESCRIPTION[1];

        int i = 0;
        int longDescOffset = (availableOptions.size()>3? 1 : 0);
        for (Options op: availableOptions) {
            switch (op) {
                case INFLAME:
                    desc += EXTENDED_DESCRIPTION[2 + longDescOffset];
                    break;
                case FOOTWORK:
                    desc += EXTENDED_DESCRIPTION[4 + longDescOffset];
                    break;
                case METALLICIZE:
                    desc += EXTENDED_DESCRIPTION[6 + longDescOffset];
                    break;
                case MACHINE_LEARNING:
                    desc += EXTENDED_DESCRIPTION[8 + longDescOffset];
                    break;
                case WELLLAID_PLANS:
                    desc += EXTENDED_DESCRIPTION[10 + longDescOffset];
                    break;
                case CALTROPS:
                    desc += EXTENDED_DESCRIPTION[12 + longDescOffset];
                    break;
                case BATTLE_HYMN:
                    desc += EXTENDED_DESCRIPTION[14 + longDescOffset];
                    break;
            }
            i++;
            if (i < availableOptions.size()) {
                if (availableOptions.size() > 2)
                    desc += EXTENDED_DESCRIPTION[16]; // "; "
                if (i == availableOptions.size() - 1) {
                    if (magicNumber < availableOptions.size())
                        desc += EXTENDED_DESCRIPTION[17]; // " or "
                    else
                        desc += EXTENDED_DESCRIPTION[18]; // " and "
                }
            } else {
                desc += EXTENDED_DESCRIPTION[19]; // "."
            }

        }

        return desc;
    }

//    public static void initializeUpgradeOrder(Random rand) {
//        Collections.shuffle(upgradeOrder, rand);
//        WidowMod.logger.info("Module \"X\" randomized. The upgrade order is:");
//        for (Options o: upgradeOrder)
//            WidowMod.logger.info(o);
//    }
}