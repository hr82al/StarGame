package ru.geekbrains.hr82al.sprits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.sun.org.apache.bcel.internal.generic.ALOAD;

import ru.geekbrains.hr82al.base.Sprite;
import ru.geekbrains.hr82al.base.ActionListener;

public class ScaledButton extends Sprite {
    private ActionListener actionListener;
    private int buttonPosition;
    private boolean pressed = false;
    private int pointer;

    public ScaledButton(TextureAtlas atlas, String buttonName, int buttonPosition) {
        super(atlas.findRegion(buttonName));
        this.buttonPosition = buttonPosition;
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

        /*if (isMe(touch) && pressed) {
            pressed = false;
            if (buttonName.equals("btExit")) {
                Gdx.app.exit();
            }
        }*/
        return false;
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }
}
