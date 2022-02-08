package theWidow.cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.BaseModCardTags;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.TransformCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.TheWidow;
import theWidow.WidowMod;
import theWidow.actions.WidowUpgradeCardAction;
import theWidow.util.TextureLoader;

import static theWidow.WidowMod.makeCardPath;
import static theWidow.WidowMod.makePowerPath;

public class TranscendentForm extends CustomCard {

    public static final String ID = WidowMod.makeID(TranscendentForm.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("TranscendentForm.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWidow.Enums.COLOR_BLACK;

    private static final int COST = 3;
    private static final int UPGRADES = 1;
    private static final int UPGRADE_PLUS_UPGRADES = 1;

    public TranscendentForm() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = UPGRADES;
        tags.add(BaseModCardTags.FORM);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new TranscensionPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_UPGRADES);
            initializeDescription();
        }
    }

    public static class TranscensionPower extends AbstractPower implements CloneablePowerInterface {
        public static final String POWER_ID = WidowMod.makeID(TranscensionPower.class.getSimpleName());
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("TranscensionPower84.png"));
        private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("TranscensionPower32.png"));

        public TranscensionPower(final AbstractCreature owner, final int amount) {
            name = powerStrings.NAME;
            ID = POWER_ID;

            this.owner = owner;
            this.amount = amount;

            type = PowerType.BUFF;
            isTurnBased = false;

            this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

            updateDescription();
        }

        @Override
        public void updateDescription() {
            if(amount == 1)
                description = DESCRIPTIONS[0] + DESCRIPTIONS[3];
            else
                description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + amount + DESCRIPTIONS[3];
        }

        @Override
        public void atStartOfTurnPostDraw() {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    for (int i=0; i< AbstractDungeon.player.hand.size(); i++)
                        if (AbstractDungeon.player.hand.group.get(i).type == CardType.STATUS || AbstractDungeon.player.hand.group.get(i).type == CardType.CURSE)
                            addToTop(new TransformCardInHandAction(i, AbstractDungeon.returnTrulyRandomCardInCombat()));
                    isDone = true;
                }
            });
            for (int i=0; i<amount; i++)
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        flash();
                        for (AbstractCard c : AbstractDungeon.player.hand.group)
                            if (c.canUpgrade())
                                addToTop(new WidowUpgradeCardAction(c));
                        isDone = true;
                    }
                });
        }

        @Override
        public AbstractPower makeCopy() {
            return new TranscensionPower(owner, amount);
        }
    }
}
