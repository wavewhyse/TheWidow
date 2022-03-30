package theWidow.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import theWidow.WidowMod;
import theWidow.util.Wiz;

public class GrenadierPower extends AbstractEasyPower implements CloneablePowerInterface {
    public static final String POWER_ID = WidowMod.makeID(GrenadierPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public GrenadierPower(AbstractCreature owner, int amount) {
        super( powerStrings.NAME,
                PowerType.BUFF,
                owner,
                amount );
    }

    protected GrenadierPower(String name, AbstractCreature owner, int amount) {
        super( name,
                PowerType.BUFF,
                owner,
                amount );
    }

    @Override
    public void onInitialApplication() {
        addToTop(new AddPotionSlotAction(amount));
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        addToTop(new AddPotionSlotAction(stackAmount));
    }

    private static class AddPotionSlotAction extends AbstractGameAction {
        public AddPotionSlotAction(int amount) {
            this.amount = amount;
        }

        @Override
        public void update() {
            Wiz.adp().potionSlots += amount;
            for (int i = 0; i < amount; i++) {
                Wiz.adp().potions.add(new PotionSlot(Wiz.adp().potionSlots - 1 - i));
            }
            isDone = true;
        }
    }

    @Override
    public void onVictory() {
        AbstractPlayer p = Wiz.adp();
        p.potionSlots -= amount;
        /*
            A case where arrays starting at 0 SUCKS and IS CONFUSING
            Okay so, this starts at the *last index* of the potions arraylist: size - 1.
            It decrements until it is at the index equal to the *new* size that
            the array SHOULD be. And it removes that, leaving all indices intact
            from 0 through the new size minus 1; which is correct.
            Why the FDUCK is potionSlots not just equal to potions.size() anyway i hate this
            NOTE: none of this comment is relevant anymore but i am keeping it regardless
        */
        if (p.potions.size() > p.potionSlots) {
            for (int i = p.potionSlots; i < p.potions.size(); i++)
                if (!(p.potions.get(i) instanceof PotionSlot)) {
                    RewardItem salvage = new RewardItem(p.potions.get(i));
                    salvage.text += powerStrings.DESCRIPTIONS[2];
                    AbstractDungeon.getCurrRoom().rewards.add(salvage);
                }
            p.potions.subList(p.potionSlots, p.potions.size()).clear();
        }
    }

    @Override
    public void onRemove() {
        onVictory();
    }

    @Override
    public void updateDescription() {
        if (amount == 1)
            description = String.format(powerStrings.DESCRIPTIONS[0], amount);
        else
            description = String.format(powerStrings.DESCRIPTIONS[1], amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new GrenadierPower(owner, amount);
    }
}
