package theWidow.deprecated;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import theWidow.WidowMod;
import theWidow.cards.BetaCard;
import theWidow.characters.TheWidow;
import theWidow.relics.SewingKitRelic;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
public class MakeshiftArmor extends BetaCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(MakeshiftArmor.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("MakeshiftArmor.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int PLATE = 4;
    private static final int UPGRADE_PLUS_PLATE = 1;

    // /STAT DECLARATION/

    public MakeshiftArmor() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = PLATE;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SewingKitRelic.ID))
            upgrade();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        upgradeMagicNumber(UPGRADE_PLUS_PLATE);
        upgradeName();
    }

    @Override
    public void downgrade() {
        magicNumber = baseMagicNumber = baseMagicNumber - UPGRADE_PLUS_PLATE;
        super.downgrade();
    }
}
