package theWidow.potions;

import basemod.abstracts.CustomPotion;
import basemod.abstracts.CustomSavable;
import basemod.helpers.CardPowerTip;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import theWidow.TheWidow;
import theWidow.WidowMod;

public class DistilledCardPotion extends CustomPotion implements CustomSavable<CardSave> {

    public static final String POTION_ID = WidowMod.makeID(DistilledCardPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public static final Color LIQUID_COLOR = Color.BLACK;
    public static final Color HYBRID_COLOR = Color.PURPLE;
    public static final Color SPOTS_COLOR = Color.MAGENTA;

    AbstractCard card;

    public DistilledCardPotion() {
        super(NAME, POTION_ID, TheWidow.Enums.BOMB, PotionSize.CARD, PotionColor.WHITE);
        initializeData();
    }

    public DistilledCardPotion(AbstractCard card) {
        super(NAME + card.name, POTION_ID, TheWidow.Enums.BOMB, PotionSize.CARD, PotionColor.WHITE);
        this.card = card;
        initializeData();
    }

    public DistilledCardPotion(CardSave save) {
        super(NAME + save.id, POTION_ID, TheWidow.Enums.BOMB, PotionSize.CARD, PotionColor.WHITE);
        this.card = CardLibrary.getCopy(save.id, save.upgrades, save.misc);
        name = NAME + card.name;
        initializeData();
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        if (card == null)
            return;
        card.resetAttributes();
        switch (card.target) {
            case ENEMY:
            case SELF_AND_ENEMY:
                isThrown = true;
                targetRequired = true;
                break;
            case ALL:
            case ALL_ENEMY:
                isThrown = true;
                targetRequired = false;
                break;
            default:
                isThrown = false;
                targetRequired = false;
                break;
        }
        tips.clear();
        if (potency > 1)
            description = DESCRIPTIONS[2] + potency + " " + card.name + DESCRIPTIONS[3];
        else
            description = DESCRIPTIONS[0] + card.name + DESCRIPTIONS[1];
//        description = "";
//        for (DescriptionLine line : card.description) {
//            description += line.getCachedTokenizedText();
//        }
        tips.add(new PowerTip(name, description));
        tips.add(new CardPowerTip(card));
    }

    @Override
    public void use(AbstractCreature target) {
        for (int i=0; i<potency; i++) {
            AbstractCard playing = card.makeStatEquivalentCopy();
            playing.purgeOnUse = true;
            playing.dontTriggerOnUseCard = true;
            AbstractDungeon.player.limbo.addToBottom(playing);
            if (target instanceof AbstractMonster)
                playing.calculateCardDamage((AbstractMonster) target);
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(playing, (AbstractMonster) target, playing.energyOnUse, true, true), true);
        }
    }

    @Override
    public AbstractPotion makeCopy() {
        return new DistilledCardPotion(card);
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        return 1;
    }

    @Override
    public CardSave onSave() {
        return new CardSave(card.cardID, card.timesUpgraded, card.misc);
    }

    @Override
    public void onLoad(CardSave save) {
        this.card = CardLibrary.getCopy(save.id, save.upgrades, save.misc);
        name = NAME + card.name;
        initializeData();
    }
}
