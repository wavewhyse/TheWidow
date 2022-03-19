package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class SludgeWave extends CustomCard {
    public static final String ID = WidowMod.makeID(SludgeWave.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public SludgeWave() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(SludgeWave.class.getSimpleName()),
                1,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.ALL );
        magicNumber = baseMagicNumber = 2;
        baseDamage = 8;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(makeSludgeEffect()));
        addToBot(new VFXAction(makeSludgeEffect()));
        addToBot(new VFXAction(makeSludgeEffect()));
        addToBot(new VFXAction(makeSludgeEffect()));
        addToBot(new WaitAction(1f));

        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));

        Wiz.apply(new VulnerablePower(p, magicNumber, false));
        Wiz.apply(new WeakPower(p, magicNumber, false));
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            Wiz.apply(new VulnerablePower(mon, magicNumber, false));
            Wiz.apply(new WeakPower(mon, magicNumber, false));
        }
//        if (Wiz.getEnemies().size() >= 3)
//            addToBot(new StunMonsterAction(Collections.max(Wiz.getEnemies(), Comparator.comparingInt(a -> a.currentHealth)), p));
    }

    public static AbstractGameEffect makeSludgeEffect() {
        return new VfxBuilder(ImageMaster.POWER_UP_2, 2F)
                .setColor(Color.PURPLE.cpy().mul(0.1F,0.1F,0.1F,1))
                .useAdditiveBlending()
                .setScale(50.0F)
                //.andThen((float) (Math.random() * 0.2F))
                .fadeIn(1F)
                .moveX((float) (Math.random() * -500) + 300F, (float) (Settings.WIDTH + Math.random() * 500))
                .oscillateY(-600F, (float) (Math.random() * -400F), 100F)
                .andThen(1F)
                .fadeOut(1F)
                .build();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(3);
            initializeDescription();
        }
    }
}