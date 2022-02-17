package theWidow.cards;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.WebPower;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makePowerPath;

public class Conduit extends CustomCard {

    public static final String ID = WidowMod.makeID(Conduit.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("Conduit.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int WEB = 3;
    private static final int UPGRADE_PLUS_WEB = 2;

    public Conduit() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = WEB;
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new WebPower(p, magicNumber), magicNumber));

        damage = magicNumber;
        if (p.hasPower(WebPower.POWER_ID))
            damage += p.getPower(WebPower.POWER_ID).amount;
        baseDamage = damage;
        calculateCardDamage(m);
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_VERTICAL));

//        addToBot(new ApplyPowerAction(p, p, new ConduitPower(p, 1), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_WEB);
        }
    }

    @Deprecated
    public static class ConduitPower extends AbstractPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(ConduitPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ConduitPower84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ConduitPower32.png"));

        public ConduitPower(final AbstractCreature owner, final int amount) {
            name = powerStrings.NAME;
            ID = POWER_ID;

            this.owner = owner;
            this.amount = amount;
            type = PowerType.BUFF;
            isTurnBased = false;

            this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

            updateDescription();
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0];
        }

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type) {
            if (type == DamageInfo.DamageType.NORMAL && owner.hasPower(WebPower.POWER_ID))
                return damage + owner.getPower(WebPower.POWER_ID).amount;
            else return damage;
        }

        @Override
        public void onAfterUseCard(AbstractCard card, UseCardAction action) {
            if (card.type == CardType.ATTACK) {
                if (amount == 1)
                    addToBot(new RemoveSpecificPowerAction(owner, owner, this));
                else
                    addToBot(new ReducePowerAction(owner, owner, this, 1));
            }
        }

        //        @Override
//        public int onAttacked(DamageInfo info, int damageAmount) {
//            if (owner.hasPower(OldWebPower.POWER_ID))
//                addToTop(new DamageAction(info.owner, new DamageInfo(owner, owner.getPower(OldWebPower.POWER_ID).amount, DamageInfo.DamageType.THORNS)));
//            return damageAmount;
//        }

//        @Override
//        public void atStartOfTurn() {
//            addToBot(new ReducePowerAction(owner, owner, this, 1));
//        }

        /*@Override
        public void atEndOfRound() {
            if (this.amount == 1) {
                addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            } else {
                addToBot(new ReducePowerAction(owner, owner, this, 1));
            }
        }

        @Override
        public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
            if (owner.hasPower(OldWebPower.POWER_ID) && info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0) {
                flash();
                addToBot(new DamageAction(target, new DamageInfo(owner, owner.getPower(OldWebPower.POWER_ID).amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
            }
        }*/

        /*@Override
        public float atDamageGive(float damage, DamageInfo.DamageType type) {
            if (type == DamageInfo.DamageType.NORMAL && owner.hasPower(OldWebPower.POWER_ID))
                return damage + owner.getPower(OldWebPower.POWER_ID).amount;
            else return damage;
        }*/

        @Override
        public AbstractPower makeCopy() {
            return new ConduitPower(owner, amount);
        }
    }
}
