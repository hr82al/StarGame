package ru.geekbrains.hr82al.sprits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.base.Sprite;

public class ScaledButton extends Sprite {
    private int buttonPosition;
    private String buttonName;
    private boolean pressed = false;

    public ScaledButton(TextureAtlas atlas, String buttonName, int buttonPosition) {
        super(atlas.findRegion(buttonName));
        this.buttonPosition = buttonPosition;
        this.buttonName = buttonName;
        setHeightProportion(0.15f);
    }

    public void resize() {
        pos.set(0f ,-0.34f + 0.3f * buttonPosition + getHalfHeight());
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if(isMe(touch)) {
            scale = 0.8f;
            pressed = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        scale = 1f;
        if (isMe(touch) && pressed) {
            if (buttonName.equals("btExit")) {
                Gdx.app.exit();
            }
        }
        return false;
    }
}
