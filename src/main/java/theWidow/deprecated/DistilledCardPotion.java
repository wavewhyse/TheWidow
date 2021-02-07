package theWidow.deprecated;

import basemod.abstracts.CustomPotion;
import basemod.abstracts.CustomSavable;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import theWidow.WidowMod;
import theWidow.characters.TheWidow;

import java.lang.reflect.Type;

@Deprecated
public class DistilledCardPotion extends CustomPotion implements CustomSavable<CardSave> {

    public static final String POTION_ID = WidowMod.makeID(DistilledCardPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    AbstractCard card;

    public DistilledCardPotion(AbstractCard card) {
        super(NAME, POTION_ID, TheWidow.Enums.BOMB, PotionSize.CARD, PotionColor.WHITE);
        
        potency = getPotency();

        this.card = card;
        isThrown = false;

        description = DESCRIPTIONS[0] + card.name + DESCRIPTIONS[2];
        
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature target) {
        for (int i = 0; i<potency; i++)
            addToBot(new MakeTempCardInHandAction(card));
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new DistilledCardPotion(card);
    }

    @Override
    public int getPotency(final int potency) {
        return 1;
    }

    public void upgradePotion() {
      potency *= 2;
      description = DESCRIPTIONS[1] + card.name + DESCRIPTIONS[2];
      tips.clear();
      tips.add(new PowerTip(name, description));
    }

    @Override
    public CardSave onSave() {
        System.out.println("saving card: " + card.cardID + " + " + card.timesUpgraded + " + " + card.misc);
        return new CardSave(card.cardID, card.timesUpgraded, card.misc);
    }

    @Override
    public void onLoad(CardSave save) {
        System.out.println("loading card: " + save.id + " + " + save.upgrades + " + " + save.misc);
        this.card = CardLibrary.getCopy(save.id, save.upgrades, save.misc);
    }

    @Override
    public Type savedType() {
        return new TypeToken<DistilledCardPotion>(){}.getType();
    }
}
