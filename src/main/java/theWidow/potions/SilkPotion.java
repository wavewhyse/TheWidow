package theWidow.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import theWidow.WidowMod;
import theWidow.powers.WebPower;
import theWidow.util.Wiz;

public class SilkPotion extends UpgradeablePotion {
    public static final String POTION_ID = WidowMod.makeID(SilkPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public SilkPotion() {
        this(false);
    }

    public SilkPotion(boolean upgraded) {
        super( potionStrings.NAME,
                POTION_ID,
                PotionRarity.COMMON,
                PotionSize.SNECKO,
                PotionEffect.NONE,
                Color.WHITE,
                Color.WHITE,
                Color.WHITE,
                upgraded );
    }

    @Override
    public void initializeData() {
        labOutlineColor = WidowMod.WIDOW_BLACK;

        potency = getPotency();

        description = String.format(potionStrings.DESCRIPTIONS[0], potency);

        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature target) {
        if (Wiz.isInCombat()) {
            Wiz.apply(new WebPower(Wiz.adp(), potency));
        }
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new SilkPotion(upgraded);
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        if (upgraded)
            return 8;
        else
            return 5;
    }

    public void upgradePotion()
    {
      potency *= 2;
      tips.clear();
      tips.add(new PowerTip(name, description));
    }
}
