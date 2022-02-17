package theWidow.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeCardAction;
import theWidow.relics.SewingKitRelic;

import static theWidow.WidowMod.makeCardPath;

public class FissionAnvil extends BetaCard {

    public static final String ID = WidowMod.makeID(FissionAnvil.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("FissionAnvil.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int DAMAGE = 20;
    private static final int UPGRADE_PLUS_DMG = 4;

    private boolean actionQueued;

    public FissionAnvil() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SewingKitRelic.ID))
            upgrade();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null)
            addToBot(new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY), Settings.ACTION_DUR_LONG));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (actionQueued
                && AbstractDungeon.getCurrMapNode() != null
                && AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && AbstractDungeon.player.hand.contains(this)) {
            superFlash();
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (!(c instanceof FissionAnvil) && c.canUpgrade())
                    addToTop(new WidowUpgradeCardAction(c));
            }
            actionQueued = false;
        }
    }

    @Override
    public void upgrade() {
        upgradeDamage(UPGRADE_PLUS_DMG + timesUpgraded);
        upgradeName();
        actionQueued = true;
    }

    @Override
    public void downgrade() {
        super.downgrade();
        baseDamage -= UPGRADE_PLUS_DMG + timesUpgraded;
    }
}
