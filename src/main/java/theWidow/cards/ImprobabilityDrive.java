package theWidow.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.relics.SewingKitRelic;

import static theWidow.WidowMod.makeCardPath;

public class ImprobabilityDrive extends BetaCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(ImprobabilityDrive.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = makeCardPath("ImprobabilityDrive.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = -1;
    private static final int ENERGY = 2;

    // /STAT DECLARATION/

    public ImprobabilityDrive() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = ENERGY;
        exhaust = true;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SewingKitRelic.ID))
            upgrade();
        else
            initializeDescription();
    }

    @Override
    public void initializeDescription() {
        rawDescription = cardStrings.DESCRIPTION;
        for (int i=0; i< magicNumber; i++)
            rawDescription = rawDescription + " [E]";
        rawDescription += EXTENDED_DESCRIPTION[0];
        super.initializeDescription();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        class DrawXCardsAction extends AbstractGameAction {
            private final float DURATION = Settings.ACTION_DUR_FAST;
            public DrawXCardsAction() {
                actionType = ActionType.DRAW;
                duration = DURATION;
            }
            @Override
            public void update() {
                int effect = EnergyPanel.totalCount;
                if (energyOnUse != -1)
                    effect = energyOnUse;
                if (p.hasRelic(ChemicalX.ID)) {
                    effect += ChemicalX.BOOST;
                    p.getRelic(ChemicalX.ID).flash();
                }
                addToBot(new DrawCardAction(effect));
                if (!freeToPlayOnce)
                    p.energy.use(EnergyPanel.totalCount);
                isDone = true;
            }
        }
        addToBot(new DrawXCardsAction());
        addToBot(new GainEnergyAction(magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        upgradeMagicNumber(1);
        upgradeName();
        initializeDescription();
    }

    @Override
    public void downgrade() {
        super.downgrade();
        magicNumber = baseMagicNumber -= 1;
    }
}
