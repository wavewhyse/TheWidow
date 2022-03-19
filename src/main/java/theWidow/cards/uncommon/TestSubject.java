package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.Wiz;

public class TestSubject extends CustomCard {
    public static final String ID = WidowMod.makeID(TestSubject.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public TestSubject() {
        super( ID,
                cardStrings.NAME,
                WidowMod.makeCardPath(TestSubject.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.ENEMY );
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractPower pow: m.powers)
            if (pow.type == AbstractPower.PowerType.DEBUFF && pow instanceof CloneablePowerInterface)
                for (AbstractMonster mon: Wiz.getEnemies()) {
                    AbstractPower cpy = ((CloneablePowerInterface) pow).makeCopy();
                    cpy.owner = mon;
                    Wiz.apply(cpy);
                }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            exhaust = false;
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
