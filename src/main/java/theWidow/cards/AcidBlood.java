package theWidow.cards;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.SapPower;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
public class AcidBlood extends CustomCard {
    public static final String ID = WidowMod.makeID(AcidBlood.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int SAPPED = 8;
    private static final int UPGRADE_PLUS_SAPPED = 2;



    public AcidBlood() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(AcidBlood.class.getSimpleName()),
                0,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.SELF );
        magicNumber = baseMagicNumber = SAPPED;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mon: AbstractDungeon.getMonsters().monsters)
            Wiz.apply(new SapPower(mon, magicNumber));
        Wiz.apply(new VulnerablePower(p, 2, false));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_SAPPED);
        }
    }
}
