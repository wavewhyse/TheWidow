package theWidow.cards.uncommon;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.WidowMod;
import theWidow.actions.EasyChooseCardAction;
import theWidow.actions.EasyXCostAction;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class ParticleAccelerator extends CustomCard {
    public static final String ID = WidowMod.makeID(ParticleAccelerator.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public ParticleAccelerator() {
        super( ID,
                cardStrings.NAME,
                makeCardPath("DischargeBattery"),
                -1,
                cardStrings.DESCRIPTION,
                CardType.SKILL,
                theWidow.TheWidow.Enums.COLOR_BLACK,
                CardRarity.UNCOMMON,
                CardTarget.SELF );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new EasyChooseCardAction(1, card -> card.costForTurn == 0, uiStrings.TEXT[0], (card, params) -> {
            addToTop(new EasyXCostAction((AbstractCard) params[0], (effect, params2) -> {
                if ((boolean)params[1])
                    effect++;
                if (effect <= 0)
                    return true;
                p.hand.group.remove(card);
                AbstractDungeon.getCurrRoom().souls.remove(card);
                p.limbo.group.add(card);
                card.current_y = -200.0F * Settings.scale;
                card.target_x = (float)Settings.WIDTH / 2.0F + 200.0F * Settings.xScale;
                card.target_y = (float)Settings.HEIGHT / 2.0F;
                card.targetAngle = 0.0F;
                card.lighten(false);
                card.drawScale = 0.12F;
                card.targetDrawScale = 0.75F;
                card.applyPowers();
                AbstractMonster target = null;
                if (card.target == CardTarget.ENEMY || card.target == CardTarget.SELF_AND_ENEMY) {
                    target = AbstractDungeon.getRandomMonster();
                }
                Wiz.adam().addCardQueueItem(new CardQueueItem(card, target, (int) params[2], true));
                addToTop(new UnlimboAction(card));
                for (int i = 1; i < effect; i++)
                    GameActionManager.queueExtraCard(card, AbstractDungeon.getRandomMonster());
                return true;
            }));
            return true;
        }, this, upgraded, energyOnUse));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }
}
