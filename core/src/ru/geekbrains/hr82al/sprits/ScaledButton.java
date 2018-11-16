package ru.geekbrains.hr82al.sprits;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.base.Sprite;
import ru.geekbrains.hr82al.base.ActionListener;

public class ScaledButton extends Sprite {
    private ActionListener actionListener;
    private boolean pressed = false;
    private int pointer;
    private static final float PRESSED_SCALE = 0.8f;

    public ScaledButton(TextureAtlas atlas, String buttonName, int buttonPosition) {
        super(atlas.findRegion(buttonName));
        this.pos.set(0f ,-0.34f + 0.3f * buttonPosition + getHalfHeight());
        setHeightProportion(0.15f);
    }

    public ScaledButton(TextureRegion region) {
        super(region);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if(isMe(touch)) {
            scale = PRESSED_SCALE;
            pressed = true;
            this.pointer = pointer;
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        scale = 1f;
        if (isMe(touch) && pressed && this.pointer == pointer) {
            pressed = false;
            if (actionListener != null) {
                actionListener.actionPerformed(this);
            }
        }
        return false;
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }
}
