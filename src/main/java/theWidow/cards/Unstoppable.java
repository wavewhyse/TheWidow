package theWidow.cards;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.OddMushroom;
import com.megacrit.cardcrawl.relics.PaperCrane;
import com.megacrit.cardcrawl.relics.PaperFrog;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makePowerPath;

public class Unstoppable extends CustomCard {

    public static final String ID = WidowMod.makeID(Unstoppable.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Attack.png");// "public static final String IMG = makeCardPath("Unstoppable.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int DURATION = 2;
    private static final int UPGRADE_PLUS_DURATION = 1;

    public Unstoppable() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DURATION;
        if (CardLibrary.getAllCards() != null && !CardLibrary.getAllCards().isEmpty())
            theWidow.util.artHelp.CardArtRoller.computeCard(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new InflameEffect(p)));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        addToBot(new ApplyPowerAction(p, p, new UnstoppablePower(p, magicNumber), magicNumber));
//        addToBot(new RemoveSpecificPowerAction(p, p, VulnerablePower.POWER_ID));
//        addToBot(new RemoveSpecificPowerAction(p, p, WeakPower.POWER_ID));
//        addToBot(new RemoveSpecificPowerAction(p, p, FrailPower.POWER_ID));
    }

    public static class UnstoppablePower extends AbstractPower implements CloneablePowerInterface {

        public static final String POWER_ID = WidowMod.makeID(UnstoppablePower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("UnstoppablePower84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("UnstoppablePower32.png"));

        public UnstoppablePower(AbstractCreature owner, int amount) {
            name = powerStrings.NAME;
            ID = POWER_ID;

            this.owner = owner;
            this.amount = amount;

            type = PowerType.BUFF;
            isTurnBased = true;

            this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

            updateDescription();
        }

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type) {
            if (owner.hasPower(WeakPower.POWER_ID) && type == DamageInfo.DamageType.NORMAL)
                return !owner.isPlayer && AbstractDungeon.player.hasRelic(PaperCrane.ID) ? damage / 0.6F : damage / 0.75F;
            else return damage;
        }

        @Override
        public float atDamageReceive(float damage, DamageInfo.DamageType type) {
            if (owner.hasPower(VulnerablePower.POWER_ID) && type == DamageInfo.DamageType.NORMAL) {
                if (this.owner.isPlayer && AbstractDungeon.player.hasRelic(OddMushroom.ID))
                    return damage / 1.25F;
                else
                    return owner != null && !owner.isPlayer && AbstractDungeon.player.hasRelic(PaperFrog.ID) ? damage / 1.75F : damage / 1.5F;
            } else return damage;
        }

        public float modifyBlock(float blockAmount) {
            if (owner.hasPower(FrailPower.POWER_ID))
                return blockAmount / 0.75F;
            else return blockAmount;
        }

        @Override
        public void atEndOfRound() {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }

        @Override
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0];
        }

        @Override
        public AbstractPower makeCopy() {
            return new UnstoppablePower(owner, amount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_DURATION);
            initializeDescription();
        }
    }
}
