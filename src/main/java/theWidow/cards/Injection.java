package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.SapPower;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;

public class Injection extends CustomCard {

    public static final String ID = WidowMod.makeID(Injection.class.getSimpleName());
    public static final String IMG = makeCardPath("Injection.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int SAP = 4;
    private static final int UPGRADE_PLUS_SAP = 2;

    public Injection() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = SAP;
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        /*for (int i=0; i<HITS; i++) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    Injection.this.calculateCardDamage(m);
                    addToTop(new ApplyPowerAction(m, p, new NecrosisPower(m, magicNumber), magicNumber));
                    addToTop(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                    isDone = true;
                }
            });
        }*/
        addToBot(new VFXAction(new DaggerSprayEffect(AbstractDungeon.getMonsters().shouldFlipVfx())));
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.POISON));
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters)
            addToBot(new ApplyPowerAction(mon, p, new SapPower(mon, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_SAP);
            initializeDescription();
        }
    }
}
