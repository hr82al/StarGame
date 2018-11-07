package ru.geekbrains.hr82al.sprits;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.pool.BulletPool;
import ru.geekbrains.hr82al.base.Sprite;
import ru.geekbrains.hr82al.math.Rect;

public class MainShip extends Sprite {
    private Vector2 velocity0 = new Vector2(0.5f, 0f);
    private Vector2 velocity = new Vector2();
    private boolean pressedLeft;
    private boolean pressetRight;
    private BulletPool bulletPool;
    private TextureAtlas atlas;
    private Rect worldBounds;

    public MainShip(TextureAtlas atlas, BulletPool bulletPool) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        setHeightProportion(0.15f);
        this.bulletPool = bulletPool;
        this.atlas = atlas;
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setBottom(worldBounds.getBottom() + 0.05f);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(velocity,delta);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (touch.x > 0) {
            pressetRight = true;
            moveRight();
        } else {
            pressedLeft = true;
            moveLeft();
        }
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (touch.x > 0) {
            pressetRight = false;
            if (pressedLeft) {
                moveLeft();
            } else {
                stop();
            }
        } else {
            pressedLeft = false;
            if (pressetRight) {
                moveRight();
            } else {
                stop();
            }
        }
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

                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressetRight = false;
                if (pressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
            case Input.Keys.W:
            case Input.Keys.UP:
                shoot();
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

    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, atlas.findRegion("bulletMainShip"), pos,
                new Vector2(0, 0.5f), 0.01f, worldBounds, 1);
    }
}
