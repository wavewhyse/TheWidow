package theWidow.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.Wiz;

public class PulseBombPotion extends UpgradeablePotion {
    public static final String POTION_ID = WidowMod.makeID(PulseBombPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public PulseBombPotion() {
        this(false);
    }

    public PulseBombPotion(boolean upgraded) {
        super( potionStrings.NAME,
                POTION_ID,
                TheWidow.Enums.BOMB,
                PotionSize.FAIRY,
                PotionEffect.OSCILLATE,
                Color.BLUE,
                Color.WHITE,
                Color.YELLOW,
                upgraded );
    }

    @Override
    public void initializeData() {
        potency = getPotency();

        description = String.format(potionStrings.DESCRIPTIONS[0], potency, potency/2);

        isThrown = true;
        targetRequired = true;

        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature target) {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped())
                addToBot(new VFXAction(new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.1F));
        }
        addToBot(new WaitAction(0.5F));

        DamageInfo damage = new DamageInfo(Wiz.adp(), potency, DamageInfo.DamageType.THORNS);
        damage.applyEnemyPowersOnly(target);
        addToBot(new DamageAction(target, damage, AbstractGameAction.AttackEffect.FIRE));

        for (AbstractMonster m: AbstractDungeon.getMonsters().monsters) {
            if (m != target) {
                damage = new DamageInfo(Wiz.adp(), potency / 2, DamageInfo.DamageType.THORNS);
                damage.applyEnemyPowersOnly(m);
                addToBot(new DamageAction(m, damage, AbstractGameAction.AttackEffect.FIRE));
            }
        }
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new PulseBombPotion(upgraded);
    }

    // This is your potency.
    @Override
    public int getPotency(final int ascensionLevel) {
        if (upgraded)
            return 30;
        else
            return 20;
    }

    public void upgradePotion()
    {
      potency *= 2;
      tips.clear();
      tips.add(new PowerTip(name, description));
    }
}
