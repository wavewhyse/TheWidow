package theWidow.cards;

import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.TheWidow;
import theWidow.WidowMod;

public class TestSubject extends CustomCard {

    public static final String ID = WidowMod.makeID(TestSubject.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = WidowMod.makeCardPath("TestSubject.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;

    public TestSubject() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, TheWidow.Enums.COLOR_BLACK, RARITY, TARGET);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new TestSubjectAction(m));
    }

    public class TestSubjectAction extends AbstractGameAction {

        public TestSubjectAction(AbstractCreature target) {
            this.target = target;
        }

        @Override
        public void update() {
            for (AbstractPower pow: target.powers) {
                if (pow.type == AbstractPower.PowerType.DEBUFF) {
                    for (AbstractMonster mon: AbstractDungeon.getMonsters().monsters)
                        addToTop(new ApplyPowerAction(mon, AbstractDungeon.player, ((CloneablePowerInterface)pow).makeCopy()));
                }
            }
            isDone = true;
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }
}
