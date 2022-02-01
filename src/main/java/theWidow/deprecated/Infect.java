package theWidow.deprecated;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.NecrosisPower;
import theWidow.util.TextureLoader;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makePowerPath;

@AutoAdd.Ignore
@Deprecated
public class Infect extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Infect.class.getSimpleName());
    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("Infect.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int NECROSIS = 1;
    private static final int UPGRADE_PLUS_NECROSIS = 1;

    // /STAT DECLARATION/

    public Infect() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = NECROSIS;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot( new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.POISON));
        addToBot( new ApplyPowerAction(m, p, new InfectPower(m, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_NECROSIS);
        }
    }

    public static class InfectPower extends AbstractPower implements CloneablePowerInterface, OnReceivePowerPower {
        public static final String POWER_ID = WidowMod.makeID("InfectPower");
        private static final PowerStrings powerStrings = languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("InfectPower84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("InfectPower32.png"));

        private boolean negateOwnDebuff;

        public InfectPower(final AbstractCreature owner, final int amount) {
            name = powerStrings.NAME;
            ID = POWER_ID;

            this.owner = owner;
            this.amount = amount;

            negateOwnDebuff = false;

            type = PowerType.DEBUFF;
            isTurnBased = false;

            this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

            updateDescription();
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        }

        @Override
        public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
            if (power.type == PowerType.DEBUFF && !target.hasPower(ArtifactPower.POWER_ID)) {
                if (negateOwnDebuff) {
                    negateOwnDebuff = false;
                    return true;
                }
                negateOwnDebuff = true;
                addToTop(new ApplyPowerAction(owner, owner, new NecrosisPower(owner, amount), amount));
            }
            return true;
        }

        @Override
        public AbstractPower makeCopy() {
            return new InfectPower(owner, amount);
        }
    }
}
