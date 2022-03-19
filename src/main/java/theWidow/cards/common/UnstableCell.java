package theWidow.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.cards.BetaCard;
import theWidow.util.Wiz;

import static theWidow.WidowMod.makeCardPath;

public class UnstableCell extends BetaCard {
    public static final String ID = WidowMod.makeID(UnstableCell.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private int playsQueued;

    public UnstableCell() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(UnstableCell.class.getSimpleName()),
                2,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.COMMON,
                CardTarget.ENEMY,
                cardStrings );
        baseDamage = 16;
        playsQueued = 0;
        SewingKitCheck();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        if (playsQueued > 0) {
            AbstractCard copyBecauseTheCardQueueIsBullshit = makeSameInstanceOf();
            Wiz.adp().limbo.addToBottom(copyBecauseTheCardQueueIsBullshit);
            copyBecauseTheCardQueueIsBullshit.current_x = current_x;
            copyBecauseTheCardQueueIsBullshit.current_y = current_y;
            copyBecauseTheCardQueueIsBullshit.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            copyBecauseTheCardQueueIsBullshit.target_y = Settings.HEIGHT / 2.0F;
            copyBecauseTheCardQueueIsBullshit.purgeOnUse = true;
            Wiz.adam().addCardQueueItem(new CardQueueItem(copyBecauseTheCardQueueIsBullshit, AbstractDungeon.getRandomMonster(), energyOnUse, true, true), true);
            playsQueued--;
        }
    }

    @Override
    public void triggerWhenDrawn() {
        playsQueued = 0;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (playsQueued > 0 && Wiz.adp().hand.contains(this)) {
            superFlash();
            if (Wiz.adam().cardQueue.stream().noneMatch(cq -> cq.card.uuid == this.uuid)) {
                Wiz.adam().addCardQueueItem(new CardQueueItem(this, true, energyOnUse, true, true));
                playsQueued--;
            }
        }
    }

    @Override
    public void upgrade() {
        upgradeName();
        upgradeDamage(5);
        if (Wiz.adp() != null && Wiz.adp().hand.contains(this))
            playsQueued++;
    }

    @Override
    public void downgrade() {
        super.downgrade();
        baseDamage -= (5);
    }
}
