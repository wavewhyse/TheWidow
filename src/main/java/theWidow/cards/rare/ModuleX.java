package theWidow.cards.rare;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Smite;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.cards.BetaCard;
import theWidow.cards.special.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.OptionalLong;
import java.util.Set;

import static theWidow.WidowMod.makeCardPath;

public class ModuleX extends BetaCard implements CustomSavable<Long> {
    public static final String ID = WidowMod.makeID(ModuleX.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final int MAX_UPGRADES = 12;

    protected enum Options {
        INFLAME, FOOTWORK, METALLICIZE, MACHINE_LEARNING, WELLLAID_PLANS, CALTROPS, BATTLE_HYMN
    }

    private Random randomizer;
    private OptionalLong initialSeed;
    private boolean[] upgradesTaken;
    private Set<Options> availableOptions;

    public ModuleX() {
        this((Settings.seed != null) ? OptionalLong.of(Settings.seed + AbstractDungeon.floorNum) : OptionalLong.empty());
    }

    private ModuleX(OptionalLong initialSeed) {
        super(ID,
                cardStrings.NAME,
                makeCardPath(ModuleX.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.POWER,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.RARE,
                CardTarget.SELF,
                cardStrings );
        magicNumber = baseMagicNumber = 1;

        availableOptions = new LinkedHashSet<>();
        availableOptions.add(Options.INFLAME);
        availableOptions.add(Options.FOOTWORK);
        upgradesTaken = new boolean[MAX_UPGRADES];

        this.initialSeed = initialSeed;
        randomizer = new Random(initialSeed.orElse(System.currentTimeMillis()/500));

        SewingKitCheck();
        initializeDescription();
    }

    @Override
    public Long onSave() {
        return initialSeed.getAsLong();
    }

    @Override
    public void onLoad(Long seed) {
        downgrade();
        initialSeed = OptionalLong.of(seed);
        randomizer = new Random(seed);
        upgrade();
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
                    upgradeBaseCost(0);
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
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        if (initialSeed.isPresent())
            return new ModuleX(initialSeed);
        else
            return new ModuleX();
    }

    @Override
    public void initializeDescription() {
        if (availableOptions == null) {
            super.initializeDescription();
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (timesUpgraded < 12)
            sb.append(cardStrings.EXTENDED_DESCRIPTION[0]);  // Beta-X
        if (magicNumber < availableOptions.size())
            sb.append(cardStrings.EXTENDED_DESCRIPTION[1]);  // Choose one:

        int i = 0;
        int longDescOffset = (availableOptions.size()>3? 1 : 0);
        for (Options op: availableOptions) {
            switch (op) {
                case INFLAME:
                    sb.append(cardStrings.EXTENDED_DESCRIPTION[2 + longDescOffset]);
                    break;
                case FOOTWORK:
                    sb.append(cardStrings.EXTENDED_DESCRIPTION[4 + longDescOffset]);
                    break;
                case METALLICIZE:
                    sb.append(cardStrings.EXTENDED_DESCRIPTION[6 + longDescOffset]);
                    break;
                case MACHINE_LEARNING:
                    sb.append(cardStrings.EXTENDED_DESCRIPTION[8 + longDescOffset]);
                    break;
                case WELLLAID_PLANS:
                    sb.append(cardStrings.EXTENDED_DESCRIPTION[10 + longDescOffset]);
                    break;
                case CALTROPS:
                    sb.append(cardStrings.EXTENDED_DESCRIPTION[12 + longDescOffset]);
                    break;
                case BATTLE_HYMN:
                    sb.append(cardStrings.EXTENDED_DESCRIPTION[14 + longDescOffset]);
                    break;
            }
            i++;
            if (i < availableOptions.size()) {
                if (availableOptions.size() > 2)
                    sb.append(cardStrings.EXTENDED_DESCRIPTION[16]); // "; "
                if (i == availableOptions.size() - 1) {
                    if (magicNumber < availableOptions.size())
                        sb.append(cardStrings.EXTENDED_DESCRIPTION[17]); // " or "
                    else
                        sb.append(cardStrings.EXTENDED_DESCRIPTION[18]); // " and "
                }
            } else {
                sb.append(cardStrings.EXTENDED_DESCRIPTION[19]); // "."
            }

        }

        rawDescription = sb.toString();
        super.initializeDescription();
    }
}