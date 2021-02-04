package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.WidowMod;
import theWidow.characters.TheWidow;
import theWidow.powers.NecrosisPower;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;

public class Injection extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Injection.class.getSimpleName());
    public static final String IMG = makeCardPath("Injection.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_PLUS_DMG = 1;
    private static final int NECROSIS = 2;
    private static final int UPGRADE_PLUS_NECROSIS = 1;
    private static final int HITS = 3;

    // /STAT DECLARATION/

    public Injection() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = NECROSIS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i=0; i<HITS; i++) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    addToTop(new ApplyPowerAction(m, p, new NecrosisPower(m, magicNumber), magicNumber));
                    DamageInfo info = new DamageInfo(p, baseDamage, DamageInfo.DamageType.NORMAL);
                    info.applyPowers(p, m);
                    addToTop(new DamageAction(m, info, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                    isDone = true;
                }
            });
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_NECROSIS);
            initializeDescription();
        }
    }
}
