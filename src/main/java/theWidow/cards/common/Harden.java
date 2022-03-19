package theWidow.cards.common;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.cards.BetaCard;

import static theWidow.WidowMod.makeCardPath;

public class Harden extends BetaCard {
    public static final String ID = WidowMod.makeID(Harden.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Harden() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Harden.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.COMMON,
                CardTarget.SELF,
                cardStrings );
        baseBlock = 9;
        SewingKitCheck();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
    }

    @Override
    public void upgrade() {
        upgradeBlock(3 + timesUpgraded);
        upgradeName();
    }

    @Override
    public void downgrade() {
        super.downgrade();
        baseBlock -= ( 3 + timesUpgraded );
    }
}