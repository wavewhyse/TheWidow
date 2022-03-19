package theWidow.potions;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import theWidow.WidowMod;
import theWidow.util.TexLoader;

import java.lang.reflect.Type;

public abstract class UpgradeablePotion extends AbstractPotion implements CustomSavable<Boolean> {

    private static final Texture plusTexture = TexLoader.getTexture(WidowMod.makeImagePath("ui/PotionPlus"));
    protected boolean upgraded;

    public UpgradeablePotion(String name, String id, PotionRarity rarity, PotionSize size, PotionEffect effect, Color liquidColor, Color hybridColor, Color spotsColor, boolean upgraded) {
        super(name + (upgraded?"+":""), id, rarity, size, effect, liquidColor, hybridColor, spotsColor);
        this.upgraded = upgraded;
        initializeData();
    }

    public UpgradeablePotion(String name, String id, PotionRarity rarity, PotionSize size, PotionEffect effect, Color liquidColor, Color hybridColor, Color spotsColor) {
        this(name, id, rarity, size, effect, liquidColor, hybridColor, spotsColor, false);
    }

    public UpgradeablePotion(String name, String id, PotionRarity rarity, PotionSize size, PotionColor color, boolean upgraded) {
        super(name + (upgraded?"+":""), id, rarity, size, color);
        this.upgraded = upgraded;
        initializeData();
    }

    public UpgradeablePotion(String name, String id, PotionRarity rarity, PotionSize size, PotionColor color) {
        this(name, id, rarity, size, color, false);
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (upgraded) {
            sb.setColor(Color.WHITE);
            sb.draw(plusTexture, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, scale, scale, 0, 0, 0, 64, 64, false, false);
        }
    }

    @Override
    public Boolean onSave() {
        return upgraded;
    }

    @Override
    public void onLoad(Boolean aBoolean) {
        if (aBoolean != null)
            upgraded = aBoolean;
        name = name + (upgraded?"+":"");
        initializeData();
    }

    @Override
    public Type savedType() {
        return Boolean.TYPE;
    }
}
