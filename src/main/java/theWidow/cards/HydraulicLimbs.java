package theWidow.cards;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.TextureLoader;

import java.util.HashSet;

import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makePowerPath;

public class HydraulicLimbs extends CustomCard {

    public static final String ID = WidowMod.makeID(HydraulicLimbs.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("HydraulicLimbs.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int  UPGRADED_COST = 1;

    public HydraulicLimbs() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new HydraulicsPower2(p, 1), 1));
    }

    public static class HydraulicsPower extends AbstractPower implements CloneablePowerInterface {

        public static final String POWER_ID = WidowMod.makeID(HydraulicsPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("HydraulicsPower84.png"));

        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("HydraulicsPower32.png"));
        private int attacksPlayedThisTurn;

        private HashSet<AbstractCard> affectedCards;
        public HydraulicsPower(final AbstractCreature owner, final int amount) {
            name = powerStrings.NAME;
            ID = POWER_ID;

            this.owner = owner;
            this.amount = amount;

            type = PowerType.BUFF;
            isTurnBased = false;
            attacksPlayedThisTurn = 0;
            for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
                if (c.type == CardType.ATTACK)
                    attacksPlayedThisTurn++;
            }
            affectedCards = new HashSet<>();

            this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

            updateDescription();
        }

        @Override
        public void onInitialApplication() {
            if (attacksPlayedThisTurn < amount) {
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c.type == CardType.ATTACK && affectedCards.add(c)) {
                        c.applyPowers();
                        c.setCostForTurn(c.costForTurn * 2);
                    }
                }
            }
        }

        @Override
        public void stackPower(int stackAmount) {
            if (attacksPlayedThisTurn < amount) {
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c.type == CardType.ATTACK && affectedCards.add(c)) {
                        c.applyPowers();
                        c.setCostForTurn(c.costForTurn * 2);
                        c.applyPowers();
                    }
                }
            }
        }

        @Override
        public void atStartOfTurn() {
            attacksPlayedThisTurn = 0;
            affectedCards.clear();
        }

        @Override
        public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
           if (attacksPlayedThisTurn < amount && type == DamageInfo.DamageType.NORMAL)
               return damage * 2f;
           return damage;
        }

        @Override
        public void onUseCard(AbstractCard c, UseCardAction action) {
            if (attacksPlayedThisTurn < amount && c.type == CardType.ATTACK) {
                attacksPlayedThisTurn++;
                flash();
                if (affectedCards.remove(c)) {
                    addToBot(new AbstractGameAction() {
                        @Override
                        public void update() {
                            c.setCostForTurn(c.costForTurn / 2);
                            c.applyPowers();
                            isDone = true;
                        }
                    });
                }
            }
            if (attacksPlayedThisTurn >= amount) {
                for (AbstractCard crd : affectedCards) {
                    crd.setCostForTurn(crd.costForTurn / 2);
                    crd.applyPowers();
                }
                affectedCards.clear();
            }
        }

        @Override
        public void onCardDraw(AbstractCard card) {
            if (attacksPlayedThisTurn < amount && card.type == CardType.ATTACK && affectedCards.add(card)) {
                card.applyPowers();
                card.setCostForTurn(card.costForTurn * 2);
                card.applyPowers();
            }
        }

        @Override
        public void updateDescription() {
            if (amount == 1) {
                description = DESCRIPTIONS[0] + DESCRIPTIONS[3];
            } else {
                description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3];
            }
        }

        @Override
        public AbstractPower makeCopy() {
            return new HydraulicsPower(owner, amount);
        }

    }

    public static class HydraulicsPower2 extends TwoAmountPower implements CloneablePowerInterface {

        public static final String POWER_ID = WidowMod.makeID(HydraulicsPower2.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("HydraulicsPower84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("HydraulicsPower32.png"));

        private static final int EVERY = 4;

        private float t;

        public HydraulicsPower2(final AbstractCreature owner, final int amount) {
            name = powerStrings.NAME;
            ID = POWER_ID;

            this.owner = owner;
            this.amount = amount;

            amount2 = 2;

            t = 0;

            type = PowerType.BUFF;
            isTurnBased = false;

            this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

            updateDescription();
        }

        @Override
        public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
            if (amount == EVERY)
                return damage * (float)amount2;
            return damage;
        }

        @Override
        public void onUseCard(AbstractCard c, UseCardAction action) {
            if (c.type == CardType.ATTACK) {
                if (amount == EVERY) {
                    amount = 1;
                    flash();
                }
                else {
                    amount++;
                    flashWithoutSound();
                }
                updateDescription();
            }
        }

        @Override
        public void stackPower(int stackAmount) {
            amount2 += stackAmount;
        }

        @Override
        public void update(int slot) {
            super.update(slot);
            t += Gdx.graphics.getDeltaTime();
            if (amount == EVERY && t >= 2) {
                flashWithoutSound();
                t -= 2;
            }
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0];
            if (amount != EVERY)
                description += DESCRIPTIONS[EVERY - amount];
            description += DESCRIPTIONS[4];
            if (amount2 < 10)
                description += DESCRIPTIONS[amount2 + 3];
            else
                description += amount2 + DESCRIPTIONS[13];
            description += DESCRIPTIONS[14];
        }

        @Override
        public AbstractPower makeCopy() {
            return new HydraulicsPower2(owner, amount);
        }

    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}
