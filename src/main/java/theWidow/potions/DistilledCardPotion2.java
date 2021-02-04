package theWidow.potions;

import basemod.abstracts.CustomPotion;
import basemod.helpers.CardPowerTip;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.SacredBark;
import theWidow.WidowMod;
import theWidow.characters.TheWidow;

public class DistilledCardPotion2 extends CustomPotion {

    public static final String POTION_ID = WidowMod.makeID(DistilledCardPotion2.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    AbstractCard card;

    public DistilledCardPotion2(AbstractCard card) {
        super(NAME + card.name, POTION_ID, TheWidow.Enums.BOMB, PotionSize.CARD, PotionColor.WHITE);
        this.card = card;
        initializeData();
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        if (card == null)
            return;
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
        if (AbstractDungeon.player.hasRelic(SacredBark.ID))
            description = DESCRIPTIONS[2] + potency + " " + card.name + DESCRIPTIONS[3];
        else
            description = DESCRIPTIONS[0] + card.name + DESCRIPTIONS[1];
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
        return new DistilledCardPotion2(card);
    }

    @Override
    public int getPotency(final int potency) {
        return 1;
    }
}
