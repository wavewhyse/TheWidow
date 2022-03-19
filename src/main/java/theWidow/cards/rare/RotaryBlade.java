package theWidow.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeCardAction;
import theWidow.cards.BetaCard;

import static theWidow.WidowMod.makeCardPath;

public class RotaryBlade extends BetaCard {
    public static final String ID = WidowMod.makeID(RotaryBlade.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public RotaryBlade() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(RotaryBlade.class.getSimpleName()),
                2,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.RARE,
                CardTarget.ENEMY,
                cardStrings );
        baseDamage = 4;
        magicNumber = baseMagicNumber = 3;
        SewingKitCheck();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++)
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new WidowUpgradeCardAction(this));
    }

    @Override
    public void upgrade() {
        upgradeMagicNumber(1);
        upgradeName();
    }

    @Override
    public void downgrade() {
        magicNumber = baseMagicNumber = baseMagicNumber - 1;
        super.downgrade();
    }
}