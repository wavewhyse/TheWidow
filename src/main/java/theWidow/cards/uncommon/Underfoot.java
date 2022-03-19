package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class Underfoot extends CustomCard {
    public static final String ID = WidowMod.makeID(Underfoot.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Underfoot() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Underfoot.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.ALL );
        baseBlock = 8;
        magicNumber = baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        for (AbstractMonster mon: AbstractDungeon.getMonsters().monsters) {
            if (mon.hasPower(WeakPower.POWER_ID))
                Wiz.apply(new WeakPower(mon, magicNumber, false));
            if (mon.hasPower(VulnerablePower.POWER_ID))
                Wiz.apply(new VulnerablePower(mon, magicNumber, false));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(4);
            upgradeMagicNumber(1);
            initializeDescription();
        }
    }
}
