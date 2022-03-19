package theWidow.deprecated;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.powers.AbstractEasyPower;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

@AutoAdd.Ignore
@Deprecated
public class Ambush extends CustomCard {
    public static final String ID = WidowMod.makeID(Ambush.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int  UPGRADED_COST = 1;



    public Ambush() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(Ambush.class.getSimpleName()),
                2,
                cardStrings.DESCRIPTION,
                CardType.POWER,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.SELF );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.apply(new AmbushPower(p, 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }

    public static class AmbushPower extends AbstractEasyPower implements CloneablePowerInterface{

        public static final String POWER_ID = WidowMod.makeID(AmbushPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        public AmbushPower(AbstractCreature owner, int amount) {
            super( powerStrings.NAME,
                    PowerType.BUFF,
                    owner,
                    amount );
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0];
        }

        @Override
        public void atEndOfTurn(boolean isPlayer) {
            if (isPlayer && Wiz.adam().cardsPlayedThisTurn.stream().noneMatch(c -> c.type == CardType.ATTACK)) {
                flash();
                for (AbstractCard c : Wiz.adp().hand.group) {
                    if (!c.isEthereal)
                        c.retain = true;
                }
            }
        }

        @Override
        public AbstractPower makeCopy() {
            return new AmbushPower(owner, amount);
        }
    }
}
