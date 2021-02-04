package theWidow.potions;

import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.SacredBark;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theWidow.WidowMod;
import theWidow.actions.WidowAllPurposeUpgradeAction;

public class NeurostimulantPotion extends CustomPotion {

    public static final String POTION_ID = WidowMod.makeID(NeurostimulantPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public static final Color LIQUID_COLOR = Color.SKY;
    public static final Color HYBRID_COLOR = Color.PINK;
    public static final Color SPOTS_COLOR = Color.WHITE;

    public NeurostimulantPotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main DefaultMod.java
        super(NAME, POTION_ID, PotionRarity.RARE, PotionSize.BOLT, PotionColor.ENERGY);
        
    }

    @Override
    public void initializeData() {
        labOutlineColor = WidowMod.WIDOW_BLACK;

        potency = getPotency();

        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SacredBark.ID))
            description = DESCRIPTIONS[1] + potency + DESCRIPTIONS[2];
        else
            description = DESCRIPTIONS[0];

        isThrown = false;

        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;
        // If you are in combat, permanently upgrade a card in your hand.
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT)
            addToBot(new WidowAllPurposeUpgradeAction(AbstractDungeon.player, false, potency, true));
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new NeurostimulantPotion();
    }

    // This is your potency.
    @Override
    public int getPotency(final int potency) {
        return 1;
    }

    public void upgradePotion()
    {
      potency *= 2;
      description = DESCRIPTIONS[1] + potency + DESCRIPTIONS[2];
      tips.clear();
      tips.add(new PowerTip(name, description));
    }
}
