package theWidow.cards;
import basemod.abstracts.CustomCard;

public abstract class ExtraMagicalCustomCard extends CustomCard implements Downgradeable {

    public int secondMagicNumber;
    public int baseSecondMagicNumber;
    public boolean upgradedSecondMagicNumber = false;
    public boolean isSecondMagicNumberModified = false;

    public ExtraMagicalCustomCard(final String id, final String name, final String img, final int cost, final String rawDescription, final CardType type, final CardColor color, final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    public void displayUpgrades() {
        super.displayUpgrades();
        if (upgradedSecondMagicNumber) {
            secondMagicNumber = baseSecondMagicNumber;
            isSecondMagicNumberModified = true;
        }

    }

    public void upgradeSecondMagicNumber(int amount) {
        baseSecondMagicNumber += amount;
        secondMagicNumber = baseSecondMagicNumber;
        upgradedSecondMagicNumber = true;
    }
}