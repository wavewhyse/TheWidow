//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package theWidow.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ShockEffect extends AbstractGameEffect {
    private Texture img = null;
    private int index = 0;
    private float x;
    private float y;
    private boolean flipX;
    private boolean flipY;
    private float intervalDuration;

    public ShockEffect(float x, float y) {
        this.renderBehind = false;// 20
        this.x = x;// 21
        this.y = y;// 22
        this.color = Settings.BLUE_TEXT_COLOR.cpy();// 23
        this.img = (Texture)ImageMaster.LIGHTNING_PASSIVE_VFX.get(this.index);// 24
        this.scale = MathUtils.random(5.6F, 7.0F) * Settings.scale;// 25
        this.rotation = MathUtils.random(360.0F);// 26

        this.flipX = MathUtils.randomBoolean();// 30
        this.flipY = MathUtils.randomBoolean();// 31
        this.intervalDuration = MathUtils.random(0.03F, 0.06F);// 32
        this.duration = this.intervalDuration;// 33
    }// 34

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();// 38
        if (this.duration < 0.0F) {// 39
            ++this.index;// 40
            if (this.index > ImageMaster.LIGHTNING_PASSIVE_VFX.size() - 1) {// 41
                this.isDone = true;// 42
                return;// 43
            }

            this.img = (Texture)ImageMaster.LIGHTNING_PASSIVE_VFX.get(this.index);// 45
            this.duration = this.intervalDuration;// 47
        }

    }// 49

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);// 53
        sb.setBlendFunction(770, 1);// 54
        sb.draw(this.img, this.x - 61.0F, this.y - 61.0F, 61.0F, 61.0F, 122.0F, 122.0F, this.scale, this.scale, this.rotation, 0, 0, 122, 122, this.flipX, this.flipY);// 55
        sb.setBlendFunction(770, 771);// 56
    }// 57

    public void dispose() {
    }// 61
}
