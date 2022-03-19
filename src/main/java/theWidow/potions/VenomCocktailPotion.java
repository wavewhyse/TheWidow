package theWidow.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import theWidow.WidowMod;
import theWidow.powers.SapPower;
import theWidow.util.Wiz;

public class VenomCocktailPotion extends AbstractPotion {
    public static final String POTION_ID = WidowMod.makeID(VenomCocktailPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public VenomCocktailPotion() {
        super( potionStrings.NAME,
                POTION_ID,
                PotionRarity.UNCOMMON,
                PotionSize.M,
                PotionEffect.NONE,
                Color.PURPLE,
                Color.CHARTREUSE,
                Color.FOREST );
    }

    @Override
    public void initializeData() {
        labOutlineColor = WidowMod.WIDOW_BLACK;

        potency = getPotency();

        description = String.format(potionStrings.DESCRIPTIONS[0], potency);

        isThrown = true;
        targetRequired = true;

        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature target) {
        if (Wiz.isInCombat()) {
            Wiz.apply(new SapPower(target, potency));
        }
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new VenomCocktailPotion();
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        return 8;
    }

    public void upgradePotion() {
      potency *= 2;
      tips.clear();
      tips.add(new PowerTip(name, description));
    }
}
