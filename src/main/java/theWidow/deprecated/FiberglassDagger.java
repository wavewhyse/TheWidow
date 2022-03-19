package theWidow.deprecated;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
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
public class FiberglassDagger extends BetaCard {
    public static final String ID = WidowMod.makeID(FiberglassDagger.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 3;



    public FiberglassDagger() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(FiberglassDagger.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.RARE,
                CardTarget.ENEMY,
                cardStrings );
        baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractGameAction.AttackEffect effect;
        if (baseDamage >= 30)
            effect = AbstractGameAction.AttackEffect.SLASH_HEAVY;
        else
            effect = AbstractGameAction.AttackEffect.SLASH_DIAGONAL;
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), effect));
        while(upgraded)
            downgrade();
    }

    @Override
    public void upgrade() {
        upgradeDamage(UPGRADE_PLUS_DMG + 2*timesUpgraded);
        upgradeName();
    }

    @Override
    public void downgrade() {
        super.downgrade();
        baseDamage -= (UPGRADE_PLUS_DMG + 2*timesUpgraded);
    }
}
