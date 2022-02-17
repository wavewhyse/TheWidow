package theWidow.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
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

public class MiniNukePotion extends UpgradeablePotion {

    public static final String POTION_ID = WidowMod.makeID(MiniNukePotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public static final Color LIQUID_COLOR = Color.LIME;
    public static final Color HYBRID_COLOR = Color.YELLOW;
    public static final Color SPOTS_COLOR = Color.ORANGE;

    public MiniNukePotion() {
        this(false);
    }

    public MiniNukePotion(boolean upgraded) {
        super(NAME, POTION_ID, TheWidow.Enums.BOMB, PotionSize.BOTTLE, PotionColor.POISON, upgraded);
    }

    @Override
    public void initializeData() {
        potency = getPotency();

        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];

        isThrown = true;
        targetRequired = false;

        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature target) {
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (!m.isDeadOrEscaped())
                addToBot(new VFXAction(new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.1F));
        }
        addToBot(new WaitAction(0.5F));
        addToBot(new DamageAllEnemiesAction(null,
                DamageInfo.createDamageMatrix(potency, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE)
        );
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new MiniNukePotion(upgraded);
    }

    // This is your potency.
    @Override
    public int getPotency(final int ascensionLevel) {
        if (upgraded)
            return 40;
        else
            return 30;
    }

    public void upgradePotion()
    {
      potency *= 2;
      tips.clear();
      tips.add(new PowerTip(name, description));
    }
}
