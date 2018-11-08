package ru.geekbrains.hr82al.sprits;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.pool.BulletPool;
import ru.geekbrains.hr82al.base.Sprite;
import ru.geekbrains.hr82al.math.Rect;

public class MainShip extends Sprite {
    private Vector2 velocity0 = new Vector2(0.5f, 0f);
    private Vector2 velocity = new Vector2();
    private boolean pressedLeft;
    private boolean pressedRight;
    private BulletPool bulletPool;
    private TextureAtlas atlas;
    private Rect worldBounds;
    private Sound soundShoot;

    public MainShip(TextureAtlas atlas, BulletPool bulletPool, Sound soundShoot) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        setHeightProportion(0.15f);
        this.bulletPool = bulletPool;
        this.atlas = atlas;
        this.soundShoot = soundShoot;
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setBottom(worldBounds.getBottom() + 0.05f);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(velocity, delta);
        stayInside();
    }

    private void stayInside() {
        if(getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        } else if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (touch.x > 0) {
            pressedRight = true;
            moveRight();
        } else {
            pressedLeft = true;
            moveLeft();
        }
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        System.out.println("right: " + getRight() + " bound right: " + worldBounds.getRight());
        if (touch.x > 0) {
            pressedRight = false;
            if (pressedLeft) {
                moveLeft();
            } else {
                stop();
            }
        } else {
            pressedLeft = false;
            if (pressedRight) {
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
                pressedRight = true;
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
                if (pressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
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
        soundShoot.play();
        bullet.set(this, atlas.findRegion("bulletMainShip"), pos,
                new Vector2(0, 0.5f), 0.01f, worldBounds, 1);
    }

}
