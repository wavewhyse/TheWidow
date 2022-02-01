package theWidow.potions;

import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
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

public class PulseBombPlusPotion extends CustomPotion {

    public static final String POTION_ID = WidowMod.makeID(PulseBombPlusPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(PulseBombPotion.POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public static final Color LIQUID_COLOR = Color.BLUE;
    public static final Color HYBRID_COLOR = Color.WHITE;
    public static final Color SPOTS_COLOR = Color.YELLOW;

    public PulseBombPlusPotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main DefaultMod.java
        super(NAME, POTION_ID, TheWidow.Enums.BOMB, PotionSize.FAIRY, PotionColor.STEROID);
        
        // Potency is the damage/magic number equivalent of potions.
        potency = getPotency();
        
        // Initialize the Description
        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1] + potency/2 + DESCRIPTIONS[2];
        
       // Do you throw this potion at an enemy or do you just consume it.
        isThrown = true;
        targetRequired = true;
        
        // Initialize the on-hover name + description
        tips.add(new PowerTip(name + "+", description));
        
    }

    @Override
    public void use(AbstractCreature target) { //TODO: make it not double damage vs main target
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (!m.isDeadOrEscaped())
                addToBot(new VFXAction(new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.1F));
        }
        addToBot(new WaitAction(0.5F));
        addToBot(new DamageAllEnemiesAction(null,
                DamageInfo.createDamageMatrix(potency/2, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE)
        );
        DamageInfo info = new DamageInfo(AbstractDungeon.player, potency/2, DamageInfo.DamageType.THORNS);
        info.applyEnemyPowersOnly(target);
        addToBot(new DamageAction(target, info, AbstractGameAction.AttackEffect.FIRE));
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new PulseBombPlusPotion();
    }

    // This is your potency.
    @Override
    public int getPotency(final int potency) {
        return 30;
    }

    public void upgradePotion()
    {
      potency *= 2;
      tips.clear();
      tips.add(new PowerTip(name, description));
    }
}
