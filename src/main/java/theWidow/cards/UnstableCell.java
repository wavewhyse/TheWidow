package theWidow.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theWidow.TheWidow;
import theWidow.WidowMod;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theWidow.WidowMod.makeCardPath;

public class UnstableCell extends BetaCard {

    public static final String ID = WidowMod.makeID(UnstableCell.class.getSimpleName());
    public static final String IMG = makeCardPath("UnstableCell.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int DAMAGE = 18;
    private static final int UPGRADE_PLUS_DMG = 5;

    private int playsQueued;

    public UnstableCell() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        playsQueued = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot( new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        if (playsQueued > 0) {
            AbstractCard copyBecauseTheCardQueueIsBullshit = makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(copyBecauseTheCardQueueIsBullshit);
            copyBecauseTheCardQueueIsBullshit.current_x = current_x;
            copyBecauseTheCardQueueIsBullshit.current_y = current_y;
            copyBecauseTheCardQueueIsBullshit.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            copyBecauseTheCardQueueIsBullshit.target_y = Settings.HEIGHT / 2.0F;
            copyBecauseTheCardQueueIsBullshit.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(copyBecauseTheCardQueueIsBullshit, AbstractDungeon.getRandomMonster(), energyOnUse, true, true), true);
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
        if (playsQueued > 0 && AbstractDungeon.player.hand.contains(this)) {
            superFlash();
            if (AbstractDungeon.actionManager.cardQueue.stream().noneMatch(cq -> cq.card.uuid == this.uuid)) {
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(this, true, energyOnUse, true, true));
                playsQueued--;
            }
//            else {
//                AbstractCard copyBecauseTheCardQueueIsBullshit = makeSameInstanceOf();
//                AbstractDungeon.player.limbo.addToBottom(copyBecauseTheCardQueueIsBullshit);
//                copyBecauseTheCardQueueIsBullshit.current_x = current_x;
//                copyBecauseTheCardQueueIsBullshit.current_y = current_y;
//                copyBecauseTheCardQueueIsBullshit.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
//                copyBecauseTheCardQueueIsBullshit.target_y = Settings.HEIGHT / 2.0F;
//                copyBecauseTheCardQueueIsBullshit.purgeOnUse = true;
//                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(copyBecauseTheCardQueueIsBullshit, null, energyOnUse, true, true), true);
//            }
        }
    }

    @Override
    public void upgrade() {
        upgradeName();
        upgradeDamage(UPGRADE_PLUS_DMG);
        if (AbstractDungeon.player.hand.contains(this))
            playsQueued++;
    }

    @Override
    public void downgrade() {
        super.downgrade();
        baseDamage -= (UPGRADE_PLUS_DMG);
    }
}
