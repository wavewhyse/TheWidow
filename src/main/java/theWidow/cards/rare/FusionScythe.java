package theWidow.cards.rare;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.WidowDowngradeCardAction;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class FusionScythe extends CustomCard {
    public static final String ID = WidowMod.makeID(FusionScythe.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public FusionScythe() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(FusionScythe.class.getSimpleName()),
                2,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.RARE,
                CardTarget.ALL_ENEMY );
        baseDamage = 10;
        isMultiDamage = true;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        if (Wiz.count(Wiz.adp().hand.group, c -> c.upgraded && c != this) == 1)
            rawDescription += cardStrings.EXTENDED_DESCRIPTION[1];
        else rawDescription += cardStrings.EXTENDED_DESCRIPTION[2];
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = 0;
        for (AbstractCard c : p.hand.group) {
            if (c.upgraded && c != this) {
                addToBot(new WidowDowngradeCardAction(c));
                count++;
            }
        }
        for (int i=0; i<count; i++)
            addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(4);
        }
    }
}
