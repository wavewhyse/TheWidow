package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theWidow.TheWidow;
import theWidow.WidowMod;

import static theWidow.WidowMod.makeCardPath;

public class VenomStrike extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(VenomStrike.class.getSimpleName());
    public static final String IMG = makeCardPath("VenomStrike.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int MAGIC = 2;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    // /STAT DECLARATION/

    public VenomStrike() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC;
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new
                DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.POISON)
        );
        addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -magicNumber), -magicNumber));
        if (!m.hasPower(ArtifactPower.POWER_ID))
            addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }
}
