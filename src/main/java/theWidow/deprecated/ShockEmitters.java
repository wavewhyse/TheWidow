package theWidow.deprecated;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.cards.BetaCard;
import theWidow.powers.ShockedPower;
import theWidow.relics.SewingKitRelic;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
@Deprecated
public class ShockEmitters extends BetaCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(ShockEmitters.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("ShockEmitters.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 1;
    private static final int MAGIC = 3;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    // /STAT DECLARATION/

    public ShockEmitters() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SewingKitRelic.ID))
            upgrade();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot(new ApplyPowerAction(m, p, new ShockedPower(m, magicNumber)));
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

    /*public static class ShockedPower extends AbstractPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(ShockedPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShockedPower84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShockedPower32.png"));

        public ShockedPower(final AbstractCreature owner, final int amount) {
            name = powerStrings.NAME;
            ID = POWER_ID;

            this.owner = owner;
            this.amount = amount;

            type = PowerType.DEBUFF;
            isTurnBased = true;

            this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

            updateDescription();
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
