package theWidow.cards.rare;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.potions.GrenadePotion;
import theWidow.powers.GrenadierPower;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class Anarchy extends CustomCard {
    public static final String ID = WidowMod.makeID(Anarchy.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Anarchy() {
        super( ID,
                cardStrings.NAME,
                makeCardPath("Grenadier"),
                1,
                cardStrings.DESCRIPTION,
                CardType.POWER,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.RARE,
                CardTarget.SELF);
        magicNumber = baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.apply(new GrenadierPower(p, magicNumber));
        for (int i = 0; i < p.potionSlots + magicNumber; i++)
            addToBot(new ObtainPotionAction(new GrenadePotion()));
        addToBot(new AbstractGameAction() {
            public void update() { p.adjustPotionPositions(); isDone = true; }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            initializeDescription();
        }
    }
}
