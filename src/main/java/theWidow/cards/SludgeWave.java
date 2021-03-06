package theWidow.cards;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import theWidow.TheWidow;
import theWidow.WidowMod;

import static theWidow.WidowMod.makeCardPath;

public class SludgeWave extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = WidowMod.makeID(SludgeWave.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("SludgeWave.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DEBUFFS = 2;
    private static final int UPGRADE_PLUS_DEBUFFS = 1;

    // /STAT DECLARATION/

    public SludgeWave() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG, COST, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DEBUFFS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            addToBot(new ApplyPowerAction(mon, p, new VulnerablePower(mon, magicNumber, false), magicNumber));
            addToBot(new ApplyPowerAction(mon, p, new WeakPower(mon, magicNumber, false), magicNumber));
        }
        addToBot(new ApplyPowerAction(p, p, new VulnerablePower(p, magicNumber, false), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new WeakPower(p, magicNumber, false), magicNumber));
        if (AbstractDungeon.getMonsters().monsters.stream().filter(mon -> !mon.isDeadOrEscaped()).count() >= 3) {
            AbstractMonster highest = AbstractDungeon.getRandomMonster();
            for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters)
                if (mon.currentHealth > highest.currentHealth)
                    highest = mon;
            addToBot(new StunMonsterAction(highest, p));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DEBUFFS);
            initializeDescription();
        }
    }
}