package theWidow.deprecated;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.powers.AbstractEasyPower;
import theWidow.util.TexLoader;
import theWidow.util.Wiz;

import java.util.HashSet;
import java.util.Set;

import static theWidow.WidowMod.makePowerPath;

public class HydraulicsPowerOld extends AbstractEasyPower implements CloneablePowerInterface {

    public static final String POWER_ID = WidowMod.makeID(HydraulicsPowerOld.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Texture tex84 = TexLoader.getTexture(makePowerPath("HydraulicsPower84"));

    private static final Texture tex32 = TexLoader.getTexture(makePowerPath("HydraulicsPower32"));
    private int attacksPlayedThisTurn;

    private final Set<AbstractCard> affectedCards;

    public HydraulicsPowerOld(final AbstractCreature owner, final int amount) {
super( powerStrings.NAME,
                    PowerType.BUFF,
                    owner,
                    amount );
        attacksPlayedThisTurn = 0;
        for (AbstractCard c : Wiz.adam().cardsPlayedThisTurn) {
            if (c.type == AbstractCard.CardType.ATTACK)
                attacksPlayedThisTurn++;
        }
        affectedCards = new HashSet<>();
    }

    @Override
    public void onInitialApplication() {
        if (attacksPlayedThisTurn < amount) {
            for (AbstractCard c : Wiz.adp().hand.group) {
                if (c.type == AbstractCard.CardType.ATTACK && affectedCards.add(c)) {
                    c.applyPowers();
                    c.setCostForTurn(c.costForTurn * 2);
                }
            }
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        if (attacksPlayedThisTurn < amount) {
            for (AbstractCard c : Wiz.adp().hand.group) {
                if (c.type == AbstractCard.CardType.ATTACK && affectedCards.add(c)) {
                    c.applyPowers();
                    c.setCostForTurn(c.costForTurn * 2);
                    c.applyPowers();
                }
            }
        }
    }

    @Override
    public void atStartOfTurn() {
        attacksPlayedThisTurn = 0;
        affectedCards.clear();
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        if (attacksPlayedThisTurn < amount && type == DamageInfo.DamageType.NORMAL)
            return damage * 2f;
        return damage;
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction action) {
        if (attacksPlayedThisTurn < amount && c.type == AbstractCard.CardType.ATTACK) {
            attacksPlayedThisTurn++;
            flash();
            if (affectedCards.remove(c)) {
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        c.setCostForTurn(c.costForTurn / 2);
                        c.applyPowers();
                        isDone = true;
                    }
                });
            }
        }
        if (attacksPlayedThisTurn >= amount) {
            for (AbstractCard crd : affectedCards) {
                crd.setCostForTurn(crd.costForTurn / 2);
                crd.applyPowers();
            }
            affectedCards.clear();
        }
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (attacksPlayedThisTurn < amount && card.type == AbstractCard.CardType.ATTACK && affectedCards.add(card)) {
            card.applyPowers();
            card.setCostForTurn(card.costForTurn * 2);
            card.applyPowers();
        }
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[3];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new HydraulicsPowerOld(owner, amount);
    }

}
