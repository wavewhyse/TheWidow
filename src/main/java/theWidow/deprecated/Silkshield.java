package theWidow.deprecated;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.cards.BetaCard;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
@Deprecated
public class Silkshield extends BetaCard {
    public static final String ID = WidowMod.makeID(Silkshield.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int BLOCK = 8;
    private static final int UPGRADE_PLUS_BLOCK = 4;



    public Silkshield() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Silkshield.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.SELF,
                cardStrings );
        baseBlock = BLOCK;
        SewingKitCheck();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        if (upgraded)
            downgrade();
    }

    @Override
    public void downgrade() {
        super.downgrade();
        baseBlock -= UPGRADE_PLUS_BLOCK + timesUpgraded;
    }

    @Override
    public void upgrade() {
        upgradeBlock(UPGRADE_PLUS_BLOCK + timesUpgraded);
        upgradeName();
    }
}
