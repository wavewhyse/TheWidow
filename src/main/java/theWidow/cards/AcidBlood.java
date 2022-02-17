package theWidow.cards;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.SapPower;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
public class AcidBlood extends CustomCard {

    public static final String ID = WidowMod.makeID(AcidBlood.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Default.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 0;
    private static final int SAPPED = 8;
    private static final int UPGRADE_PLUS_SAPPED = 2;

    public AcidBlood() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = SAPPED;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mon: AbstractDungeon.getMonsters().monsters)
            addToBot(new ApplyPowerAction(mon, p, new SapPower(mon, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new VulnerablePower(p, 2, false)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_SAPPED);
        }
    }
}
