package theWidow.cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import theWidow.TheWidow;
import theWidow.WidowMod;

import static theWidow.WidowMod.makeCardPath;

public class InForTheKill extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(InForTheKill.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("InForTheKill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int DAMAGE = 14;
    private static final int VULN_MULT = 2;
    private static final int UPGRADE_PLUS_VULN_MULT = 1;

    // /STAT DECLARATION/

    public InForTheKill() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = VULN_MULT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.hasPower(VulnerablePower.POWER_ID)) {
            addToBot(new VFXAction(new VfxBuilder(ImageMaster.DAGGER_STREAK, (m.hb.cX + p.drawX) / 2f, m.hb.cY, (Settings.FAST_MODE ? 0.1f : 1f))
                    .setColor(Color.RED.cpy())
                    .fadeIn(0.2f)
                    .andThen(0.4f)
                    .moveX((m.hb.cX + p.drawX) / 2f, m.hb.cX, VfxBuilder.Interpolations.SWINGIN)
                    .fadeOut(0.3f)
                    .playSoundAt(0.2f, "IN_FOR_THE_KILL")
                    .build()
            ));
            addToBot(new WaitAction(1.2f));
        }
        else
            addToBot(new VFXAction(new ThrowDaggerEffect(m.hb.cX, m.hb.cY)));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo); //TODO: Technically there are rounding errors with this method, a patch would be required to fix
        if(mo.hasPower(VulnerablePower.POWER_ID)) {
            float tmp = damage;
            for (int i = 0; i < magicNumber - 1; i++)
                tmp = mo.getPower(VulnerablePower.POWER_ID).atDamageReceive(tmp, damageTypeForTurn, this);
            damage = MathUtils.floor(tmp);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_VULN_MULT);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
