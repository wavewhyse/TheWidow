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
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
@Deprecated
public class ShockEmitters extends BetaCard {
    public static final String ID = WidowMod.makeID(ShockEmitters.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 1;
    private static final int MAGIC = 3;
    private static final int UPGRADE_PLUS_MAGIC = 1;



    public ShockEmitters() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(ShockEmitters.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.ENEMY,
                cardStrings );
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC;
        SewingKitCheck();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        Wiz.apply(new ShockedPower(m, magicNumber));
    }

    @Override
    public void upgrade() {
        upgradeName();
        upgradeDamage(UPGRADE_PLUS_DMG);
        upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
    }

    @Override
    public void downgrade() {
        super.downgrade();
        baseDamage -= UPGRADE_PLUS_DMG;
        magicNumber = baseMagicNumber -= UPGRADE_PLUS_MAGIC;
    }

    /*public static class ShockedPower extends AbstractEasyPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(ShockedPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        public ShockedPower(final AbstractCreature owner, final int amount) {
super(powerStrings.NAME, PowerType.DEBUFF, true, owner, amount);
        }

        @Override
        public void atEndOfRound() {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }

        @Override
        public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
            if (info.type == DamageInfo.DamageType.NORMAL) {
                flashWithoutSound();
                addToTop(new DamageAction(owner, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE, true));
                addToTop(new VFXAction(new LightningEffect(owner.drawX, owner.drawY), 0.1F));
                addToTop(new SFXAction("ORB_LIGHTNING_EVOKE",3.0f));
            }
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        }

        @Override
        public AbstractPower makeCopy() {
            return new ShockedPower(owner, amount);
        }
    }*/
}
