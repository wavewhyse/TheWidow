package theWidow.cards.rare;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
import theWidow.util.TexLoader;

import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makeID;

public class HydraulicLimbs extends CustomCard {
    public static final String ID = WidowMod.makeID(HydraulicLimbs.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public HydraulicLimbs() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(HydraulicLimbs.class.getSimpleName()),
                2,
                cardStrings.DESCRIPTION,
                CardType.POWER,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.RARE,
                CardTarget.SELF );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new HydraulicsPower(p, 2), 1));
    }

    public static class HydraulicsPower extends TwoAmountPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(HydraulicsPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        private static final int EVERY = 4;

        private float t;

        public HydraulicsPower(AbstractCreature owner, int amount) {
            ID = makeID(getClass().getSimpleName());
            name = powerStrings.NAME;
            this.owner = owner;
            amount2 = amount;
            this.amount = 1;
            type = PowerType.BUFF;
            t = 0;

            Texture normalTexture = TexLoader.getTexture(WidowMod.makePowerPath(getClass().getSimpleName() + "32"));
            Texture hiDefImage = TexLoader.getTexture(WidowMod.makePowerPath(getClass().getSimpleName() + "84"));
            if (hiDefImage != null) {
                region128 = new TextureAtlas.AtlasRegion(hiDefImage, 0, 0, hiDefImage.getWidth(), hiDefImage.getHeight());
                if (normalTexture != null)
                    region48 = new TextureAtlas.AtlasRegion(normalTexture, 0, 0, normalTexture.getWidth(), normalTexture.getHeight());
            } else if (normalTexture != null) {
                img = normalTexture;
                region48 = new TextureAtlas.AtlasRegion(normalTexture, 0, 0, normalTexture.getWidth(), normalTexture.getHeight());
            }

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
            fontScale = 8.0F;
            amount2 += stackAmount;
        }

        @Override
        public void update(int slot) {
            super.update(slot);
            t += Gdx.graphics.getDeltaTime();
            if (t >= 2) {
                if (amount == EVERY)
                    flashWithoutSound();
                t -= 2;
            }
        }

        @Override
        public void updateDescription() {
            StringBuilder sb = new StringBuilder(powerStrings.DESCRIPTIONS[0]);
            if (amount != EVERY)
                sb.append(powerStrings.DESCRIPTIONS[EVERY - amount]);
            sb.append(powerStrings.DESCRIPTIONS[4]);
            if (amount2 < 10)
                sb.append(powerStrings.DESCRIPTIONS[amount2 + 3]);
            else
                sb.append(String.format(powerStrings.DESCRIPTIONS[13], amount2));
            sb.append(powerStrings.DESCRIPTIONS[14]);
            description = sb.toString();
        }

        @Override
        public AbstractPower makeCopy() {
            return new HydraulicsPower(owner, amount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(1);
            initializeDescription();
        }
    }
}
