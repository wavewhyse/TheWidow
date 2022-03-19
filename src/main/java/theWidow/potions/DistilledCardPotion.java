package theWidow.potions;

import basemod.abstracts.CustomSavable;
import basemod.helpers.CardPowerTip;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.util.Wiz;

public class DistilledCardPotion extends AbstractPotion implements CustomSavable<CardSave> {
    public static final String POTION_ID = WidowMod.makeID(DistilledCardPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    private AbstractCard card;

    public DistilledCardPotion() {
        super(potionStrings.NAME, POTION_ID, TheWidow.Enums.BOMB, PotionSize.CARD, PotionEffect.OSCILLATE, Color.BLACK, Color.PURPLE, Color.MAGENTA);
        initializeData();
    }

    public DistilledCardPotion(AbstractCard card) {
        super(potionStrings.NAME + card.name, POTION_ID, TheWidow.Enums.BOMB, PotionSize.CARD, PotionEffect.OSCILLATE, Color.BLACK, Color.PURPLE, Color.MAGENTA);
        this.card = card;
        initializeData();
    }

    public DistilledCardPotion(CardSave save) {
        super(potionStrings.NAME + save.id, POTION_ID, TheWidow.Enums.BOMB, PotionSize.CARD, PotionColor.WHITE);
        this.card = CardLibrary.getCopy(save.id, save.upgrades, save.misc);
        name = potionStrings.NAME + card.name;
        initializeData();
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        if (card == null)
            card = new Madness();
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
            description = String.format(potionStrings.DESCRIPTIONS[2], potency, card.name);
        else
            description = String.format(potionStrings.DESCRIPTIONS[0], card.name);
        tips.add(new PowerTip(name, description));
        tips.add(new CardPowerTip(card));
    }

    @Override
    public void use(AbstractCreature target) {
        if (card == null)
            return;
        for (int i=0; i<potency; i++) {
            AbstractCard playing = card.makeStatEquivalentCopy();
            playing.purgeOnUse = true;
            playing.dontTriggerOnUseCard = true;
            Wiz.adp().limbo.addToBottom(playing);
            if (target instanceof AbstractMonster)
                playing.calculateCardDamage((AbstractMonster) target);
            Wiz.adam().addCardQueueItem(new CardQueueItem(playing, (AbstractMonster) target, playing.energyOnUse, true, true), true);
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
        if (card != null)
            return new CardSave(card.cardID, card.timesUpgraded, card.misc);
        else
            return new CardSave(Madness.ID, 0, 0);
    }

    @Override
    public void onLoad(CardSave save) {
        this.card = CardLibrary.getCopy(save.id, save.upgrades, save.misc);
        name = potionStrings.NAME + card.name;
        initializeData();
    }
}
