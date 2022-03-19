package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
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
import theWidow.actions.EasyXCostAction;
import theWidow.util.TexLoader;
import theWidow.util.Wiz;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makeID;

public class Gash extends CustomCard {
    public static final String ID = WidowMod.makeID(Gash.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Gash() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Gash.class.getSimpleName()),
                -1,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.ENEMY );
        baseDamage = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new EasyXCostAction(this, (effect, params) -> {
            Wiz.applyTop(new GashPower(m, (int) params[0], effect));
            return true;
            },
                damage * 2));
//        addToBot(new AbstractGameAction() {
//            @Override
//            public void update() {
//                int effect = EnergyPanel.totalCount;
//                if (energyOnUse != -1)
//                    effect = energyOnUse;
//                if (p.hasRelic(ChemicalX.ID)) {
//                    effect += ChemicalX.BOOST;
//                    p.getRelic(ChemicalX.ID).flash();
//                }
//                if (effect > 0)
//                    Wiz.applyTop(new GashPower(m, damage*2, effect));
//                if (!freeToPlayOnce)
//                    p.energy.use(EnergyPanel.totalCount);
//                isDone = true;
//            }
//        });
    }

    public static class GashPower extends TwoAmountPower implements CloneablePowerInterface, NonStackablePower {
        public static final String POWER_ID = WidowMod.makeID(GashPower.class.getSimpleName());
        private static final PowerStrings powerStrings = languagePack.getPowerStrings(POWER_ID);

        public GashPower(final AbstractCreature owner, final int damage, final int duration) {
            ID = makeID(getClass().getSimpleName());
            name = powerStrings.NAME;
            this.owner = owner;
            amount = duration;
            amount2 = damage;
            type = PowerType.DEBUFF;
            isTurnBased = true;

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
        public void atEndOfRound() {
            flash();
            addToBot(new DamageAction(owner, new DamageInfo(owner, amount2, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            if (amount > 1)
                addToBot(new ReducePowerAction(owner, owner, this, 1));
            else
                addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }

        @Override
        public void updateDescription() {
            description = String.format(powerStrings.DESCRIPTIONS[0], amount, amount2);
        }

        @Override
        public AbstractPower makeCopy() {
            return new GashPower(owner, amount2, amount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(2);
        }
    }
}
