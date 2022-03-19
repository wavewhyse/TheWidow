package theWidow.cards.rare;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.EasyXCostAction;
import theWidow.cards.BetaCard;

import static theWidow.WidowMod.makeCardPath;

public class ImprobabilityDrive extends BetaCard {
    public static final String ID = WidowMod.makeID(ImprobabilityDrive.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public ImprobabilityDrive() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(ImprobabilityDrive.class.getSimpleName()),
                -1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.RARE,
                CardTarget.SELF,
                cardStrings );
        magicNumber = baseMagicNumber = 2;
        exhaust = true;
        SewingKitCheck();
    }

    @Override
    public void initializeDescription() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i< magicNumber; i++)
            sb.append(" [E]");
        rawDescription = String.format(cardStrings.DESCRIPTION, sb);
        super.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        class DrawXCardsAction extends AbstractGameAction {
//            private final float DURATION = Settings.ACTION_DUR_FAST;
//            public DrawXCardsAction() {
//                actionType = ActionType.DRAW;
//                duration = DURATION;
//            }
//            @Override
//            public void update() {
//                int effect = EnergyPanel.totalCount;
//                if (energyOnUse != -1)
//                    effect = energyOnUse;
//                if (p.hasRelic(ChemicalX.ID)) {
//                    effect += ChemicalX.BOOST;
//                    p.getRelic(ChemicalX.ID).flash();
//                }
//                addToBot(new DrawCardAction(effect));
//                if (!freeToPlayOnce)
//                    p.energy.use(EnergyPanel.totalCount);
//                isDone = true;
//            }
//        }
        addToBot(new EasyXCostAction(this, (effect, params) -> {
            addToTop(new DrawCardAction(effect));
            return true;
        }));
        addToBot(new GainEnergyAction(magicNumber));
    }

    @Override
    public void upgrade() {
        upgradeMagicNumber(1);
        upgradeName();
        initializeDescription();
    }

    @Override
    public void downgrade() {
        super.downgrade();
        magicNumber--;
        baseMagicNumber--;
        initializeDescription();
    }
}
