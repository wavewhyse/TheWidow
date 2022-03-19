package theWidow.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.cards.BetaCard;

import static theWidow.WidowMod.makeCardPath;

public class OmniCannon extends BetaCard {
    public static final String ID = WidowMod.makeID(OmniCannon.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public OmniCannon() {
        super( ID,
                cardStrings.NAME,
                makeCardPath(OmniCannon.class.getSimpleName()),
                5,
                cardStrings.DESCRIPTION,
                CardType.ATTACK,
                TheWidow.Enums.COLOR_BLACK,
                CardRarity.RARE,
                CardTarget.ALL_ENEMY,
                cardStrings );
        baseDamage = 32;
        isMultiDamage = true;
        SewingKitCheck();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("ATTACK_HEAVY"));
        addToBot(new VFXAction(p, new MindblastEffect(p.dialogX, p.dialogY, p.flipHorizontal), 1f));
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public boolean canUpgrade() {
        return cost > 0;
    }

    @Override
    public void upgrade() {
        if (cost > 0) {
            upgradeBaseCost(cost - 1);
            upgradeName();
        }
    }

    @Override
    public void downgrade() {
        upgradeBaseCost(cost + 1);
        if (costForTurn == 0)
            costForTurn = 1;
        super.downgrade();
    }

    @Override
    public AbstractCard makeCopy() {
        return new OmniCannon();
    }
}