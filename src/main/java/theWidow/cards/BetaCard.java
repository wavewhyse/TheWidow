package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public abstract class BetaCard extends CustomCard implements Downgradeable{

    private final CardStrings cardStrings;

    public BetaCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        cardStrings = CardCrawlGame.languagePack.getCardStrings(id);
        //if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SewingKitRelic.ID))
        //    upgrade();
    }

    @Override
    public void downgrade() {
        if (upgraded) {
            timesUpgraded--;
            if (timesUpgraded <= 0) {
                upgraded = false;
                upgradedDamage = false;
                upgradedBlock = false;
                upgradedMagicNumber = false;
                name = cardStrings.NAME;
                rawDescription = cardStrings.DESCRIPTION;
                initializeDescription();
            } else
                name = cardStrings.NAME + "+" + timesUpgraded;
            initializeTitle();
        }
    }

    @Override
    public boolean canUpgrade() {
        return true;
    }

    @Override
    protected void upgradeName() {
        timesUpgraded++;
        name = cardStrings.NAME + "+" + timesUpgraded;
        upgraded = true;
        initializeTitle();
    }
}
