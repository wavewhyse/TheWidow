package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.WidowMod;
import theWidow.actions.WidowDowngradeCardAction;
import theWidow.characters.TheWidow;

import static theWidow.WidowMod.makeCardPath;

public class FusionScythe2 extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(FusionScythe2.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = makeCardPath("FusionScythe.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 3;

    // /STAT DECLARATION/

    public FusionScythe2() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        isMultiDamage = true;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        rawDescription = cardStrings.DESCRIPTION;
        rawDescription += EXTENDED_DESCRIPTION[0] + EXTENDED_DESCRIPTION[2];
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
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
