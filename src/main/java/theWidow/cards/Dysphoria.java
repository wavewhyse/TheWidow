package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.WidowMod;
import theWidow.characters.TheWidow;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;

public class Dysphoria extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(Dysphoria.class.getSimpleName());
    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("Attack.png");// "public static final String IMG = makeCardPath("Dysphoria.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.CURSE;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = -2;
    private static final int UPGRADES = 3;
    private static final int HP_INCREASE = 12;

    // /STAT DECLARATION/

    public Dysphoria() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = HP_INCREASE;
    }

    @Override
    public void onRemoveFromMasterDeck() {
        AbstractDungeon.player.increaseMaxHp(magicNumber, true);

        /*ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
        AbstractDungeon.player.masterDeck.group.stream().filter(AbstractCard::canUpgrade).forEach(upgradableCards::add);
        Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
        for (int i=0; i<UPGRADES && i < upgradableCards.size(); i++) {
            upgradableCards.get(i).upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(i));
            AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect((upgradableCards
                    .get(i)).makeStatEquivalentCopy(), (i + 1) * Settings.WIDTH / ( UPGRADES + 2f ), Settings.HEIGHT / 2.0F));
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        }*/
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void upgrade() {
    }
}
