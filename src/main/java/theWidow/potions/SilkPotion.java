package theWidow.potions;

import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theWidow.WidowMod;
import theWidow.powers.WebPower;

public class SilkPotion extends CustomPotion {

    public static final String POTION_ID = WidowMod.makeID(SilkPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public static final Color LIQUID_COLOR = Color.WHITE;
    public static final Color HYBRID_COLOR = Color.WHITE;
    public static final Color SPOTS_COLOR = Color.WHITE;

    public SilkPotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main DefaultMod.java
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.SNECKO, PotionColor.WHITE);
    }

    @Override
    public void initializeData() {
        labOutlineColor = WidowMod.WIDOW_BLACK;

        potency = getPotency();

        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];

        isThrown = false;

        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;
        // If you are in combat, gain Web.
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new WebPower(target, potency), potency));
        }
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new SilkPotion();
    }

    @Override
    public int getPotency(final int potency) {
        return 3;
    }

    public void upgradePotion()
    {
      potency *= 2;
      tips.clear();
      tips.add(new PowerTip(name, description));
    }
}
