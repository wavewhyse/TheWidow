package theWidow.deprecated;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.cards.ExtraMagicalCustomCard;

import java.util.ArrayList;
import java.util.Collections;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
@Deprecated
public class Dysphoria extends ExtraMagicalCustomCard {

    public static final String ID = WidowMod.makeID(Dysphoria.class.getSimpleName());
    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = makeCardPath("Attack.png");// "public static final String IMG = makeCardPath("Dysphoria.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.CURSE;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = -2;
    private static final int UPGRADES = 3;
    private static final int HP_INCREASE = 12;

    public Dysphoria() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = UPGRADES;
        secondMagicNumber = baseSecondMagicNumber = HP_INCREASE;
    }

    @Override
    public void onRemoveFromMasterDeck() {
        ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
        AbstractDungeon.player.masterDeck.group.stream().filter(AbstractCard::canUpgrade).forEach(upgradableCards::add);
        Collections.shuffle(upgradableCards, AbstractDungeon.miscRng.random);
        for (int i=0; i<UPGRADES && i < upgradableCards.size(); i++) {
            upgradableCards.get(i).upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(i));
            AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect((upgradableCards
                    .get(i)).makeStatEquivalentCopy(), (i + 1) * Settings.WIDTH / ( UPGRADES + 2f ), Settings.HEIGHT / 2.0F));
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        }
        AbstractDungeon.player.increaseMaxHp(secondMagicNumber, true);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void upgrade() {}
}
