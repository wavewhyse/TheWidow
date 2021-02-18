package theWidow.deprecated;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
@Deprecated
public class TranscendentForm2 extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(TranscendentForm2.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("TranscendentForm.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 3;

    // /STAT DECLARATION/

    public TranscendentForm2() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot( new ApplyPowerAction(p, p, new TransensionPower2(p, p, 1), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            isEthereal = false;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
