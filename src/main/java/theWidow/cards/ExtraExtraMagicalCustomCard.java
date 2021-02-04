package theWidow.cards;

public abstract class ExtraExtraMagicalCustomCard extends ExtraMagicalCustomCard {

    public int thirdMagicNumber;
    public int baseThirdMagicNumber;
    public boolean upgradedThirdMagicNumber = false;
    public boolean isThirdMagicNumberModified = false;

    public ExtraExtraMagicalCustomCard(final String id, final String name, final String img, final int cost, final String rawDescription, final CardType type, final CardColor color, final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    public void displayUpgrades() {
        super.displayUpgrades();
        if (upgradedThirdMagicNumber) {
            thirdMagicNumber = baseThirdMagicNumber;
            isThirdMagicNumberModified = true;
        }
    }

    public void upgradeThirdMagicNumber(int amount) {
        baseThirdMagicNumber += amount;
        thirdMagicNumber = baseThirdMagicNumber;
        upgradedThirdMagicNumber = true;
    }
}