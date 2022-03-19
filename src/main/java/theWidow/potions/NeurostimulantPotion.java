package theWidow.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.SacredBark;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeManagerAction;
import theWidow.util.Wiz;

public class NeurostimulantPotion extends AbstractPotion {

    public static final String POTION_ID = WidowMod.makeID(NeurostimulantPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public NeurostimulantPotion() {
        super(potionStrings.NAME,
                POTION_ID,
                PotionRarity.RARE,
                PotionSize.BOLT,
                PotionEffect.NONE,
                Color.SKY,
                Color.PINK,
                Color.WHITE);
    }

    @Override
    public void initializeData() {
        labOutlineColor = WidowMod.WIDOW_BLACK;

        potency = getPotency();

        if (Wiz.adp() != null && Wiz.adp().hasRelic(SacredBark.ID))
            description = String.format(potionStrings.DESCRIPTIONS[1], potency);
        else
            description = potionStrings.DESCRIPTIONS[0];

        isThrown = false;

        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature target) {
        if (Wiz.isInCombat())
            addToBot(new WidowUpgradeManagerAction(potency, false, true));
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new NeurostimulantPotion();
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        return 1;
    }

    public void upgradePotion() {
      potency *= 2;
      description = String.format(potionStrings.DESCRIPTIONS[1], potency);
      tips.clear();
      tips.add(new PowerTip(name, description));
    }
}
