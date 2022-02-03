package theWidow.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import theWidow.TheWidow;
import theWidow.WidowMod;

public class GrenadePotion extends UpgradeablePotion {

    public static final String POTION_ID = WidowMod.makeID(GrenadePotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public static final Color LIQUID_COLOR = Color.BLACK;
    public static final Color HYBRID_COLOR = Color.RED;
    public static final Color SPOTS_COLOR = Color.BLACK;

    public GrenadePotion() {
        this(false);
    }

    public GrenadePotion(boolean upgraded) {
        super(NAME, POTION_ID, TheWidow.Enums.BOMB, PotionSize.SPHERE, PotionColor.STEROID, upgraded);
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        
        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
        
        isThrown = true;
        targetRequired = true;

        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature target) {
        DamageInfo info = new DamageInfo(AbstractDungeon.player, potency, DamageInfo.DamageType.THORNS);
        info.applyEnemyPowersOnly(target);
        addToBot(new DamageAction(target, info, AbstractGameAction.AttackEffect.FIRE));
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new GrenadePotion(upgraded);
    }

    // This is your potency.
    @Override
    public int getPotency(final int ascensionLevel) {
        if (upgraded)
            return 15;
        else
            return 10;
    }

    public void upgradePotion() {
      potency *= 2;
      tips.clear();
      tips.add(new PowerTip(name, description));
    }
}
