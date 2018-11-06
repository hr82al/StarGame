package ru.geekbrains.hr82al.sprits;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.base.Sprite;
import ru.geekbrains.hr82al.math.Rect;

public class MainShip extends Sprite {
    private Vector2 velocity0 = new Vector2(0.5f, 0f);
    private Vector2 velocity = new Vector2();
    private boolean pressedLeft;
    private boolean pressetRight;

    public MainShip(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        setHeightProportion(0.15f);
    }

    @Override
    public void resize(Rect worldBounds) {
        setBottom(worldBounds.getBottom() + 0.05f);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(velocity,delta);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        return super.touchUp(touch, pointer);
    }


    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressetRight = true;
                moveRight();
                break;
        }
        return false;
    }


    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressetRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressetRight = false;
                if (pressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                stop();
                break;
        }
        return false;
    }

    private void moveRight() {
        velocity.set(velocity0);
    }

    private void moveLeft() {
        velocity.set(velocity0).rotate(180);
    }

    private void stop() {
        velocity.setZero();
    }
}
