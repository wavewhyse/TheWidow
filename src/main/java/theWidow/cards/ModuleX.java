package theWidow.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.BattleHymnPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.cards.ModuleXOptions.*;

import java.util.*;

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

    private enum Options {
        INFLAME, FOOTWORK, METALLICIZE, MACHINE_LEARNING, WELLLAID_PLANS, CALTROPS, BATTLE_HYMN, COST
    }

    private static List<Options> upgradeOrder = Arrays.asList(
            Options.INFLAME,    //always adds another to "choose x", since inflame effect is in by default.
            Options.METALLICIZE,
            Options.METALLICIZE,
            Options.MACHINE_LEARNING,
            Options.MACHINE_LEARNING,
            Options.WELLLAID_PLANS,
            Options.WELLLAID_PLANS,
            Options.CALTROPS,
            Options.CALTROPS,
            Options.BATTLE_HYMN,
            Options.BATTLE_HYMN,
            Options.COST
    );

    public static final int MAX_UPGRADES = upgradeOrder.size();

    protected Set<Options> availableOptions;

    // /STAT DECLARATION/

    public ModuleX() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        availableOptions = new LinkedHashSet<>();
        availableOptions.add(Options.INFLAME);
        availableOptions.add(Options.FOOTWORK);
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

    private AbstractGameAction getAction (AbstractPlayer p, Options opt) {
        switch (opt) {
            case INFLAME:
                return new ApplyPowerAction(p, p, new StrengthPower(p, 2), 2);
            case FOOTWORK:
                return new ApplyPowerAction(p, p, new DexterityPower(p, 2), 2);
            case METALLICIZE:
                return new ApplyPowerAction(p, p, new MetallicizePower(p, 3), 3);
            case MACHINE_LEARNING:
                return new ApplyPowerAction(p, p, new DrawPower(p, 1), 1);
            case WELLLAID_PLANS:
                return new ApplyPowerAction(p, p, new RetainCardPower(p, 1), 1);
            case CALTROPS:
                return new ApplyPowerAction(p, p, new ThornsPower(p, 3), 3);
            case BATTLE_HYMN:
                return new ApplyPowerAction(p, p, new BattleHymnPower(p, 1), 1);
            default:
                return null;
        }
    }

    @Override
    public boolean canUpgrade() {
        return timesUpgraded < MAX_UPGRADES;
    }

    @Override
    public void upgrade() {
        upgraded = true;
        if (timesUpgraded < MAX_UPGRADES) {
            Options thisUpgrade = upgradeOrder.get(timesUpgraded);
            upgradeName();
            if (thisUpgrade == Options.COST)
                upgradeBaseCost(UPGRADED_COST);
            else
                if (!availableOptions.add(thisUpgrade))
                    magicNumber = baseMagicNumber = baseMagicNumber + 1;

            rawDescription = getDescription();
            initializeDescription();
        }
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

    public static void initializeUpgradeOrder(Random rand) {
        Collections.shuffle(upgradeOrder, rand);
        WidowMod.logger.info("Module \"X\" randomized. The upgrade order is:");
        for (Options o: upgradeOrder)
            WidowMod.logger.info(o);
    }
}