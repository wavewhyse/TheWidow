package theWidow.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theWidow.WidowMod;
import theWidow.util.TexLoader;

import static theWidow.WidowMod.makeID;

public abstract class AbstractEasyPower extends AbstractPower {
    public AbstractEasyPower(String name, PowerType type, AbstractCreature owner, int amount) {
        ID = makeID(getClass().getSimpleName());
        this.name = name;
        this.owner = owner;
        this.amount = amount;
        this.type = type;

        Texture normalTexture = TexLoader.getTexture(WidowMod.makePowerPath(getClass().getSimpleName() + "32"));
        Texture hiDefImage = TexLoader.getTexture(WidowMod.makePowerPath(getClass().getSimpleName() + "84"));
        if (hiDefImage != null) {
            region128 = new TextureAtlas.AtlasRegion(hiDefImage, 0, 0, hiDefImage.getWidth(), hiDefImage.getHeight());
            if (normalTexture != null)
                region48 = new TextureAtlas.AtlasRegion(normalTexture, 0, 0, normalTexture.getWidth(), normalTexture.getHeight());
        } else if (normalTexture != null) {
            img = normalTexture;
            region48 = new TextureAtlas.AtlasRegion(normalTexture, 0, 0, normalTexture.getWidth(), normalTexture.getHeight());
        }

        updateDescription();
    }
}