package ru.geekbrains.hr82al.sprits;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.base.Ship;
import ru.geekbrains.hr82al.pool.BulletPool;
import ru.geekbrains.hr82al.math.Rect;
import ru.geekbrains.hr82al.pool.ExplosionPool;

public class MainShip extends Ship {
    private static final int INVALID_POINTER = -1;  //There aren't touches.
    public static final int MAX_HP = 100;
    public static final float HEIGHT = 0.15f;
    private Vector2 v0 = new Vector2(0.5f, 0f);
    private boolean pressedLeft;
    private boolean pressedRight;
    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;
    private TextureAtlas atlas;
    private HPIndicator hpIndicator;

    public MainShip(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool,
                    Rect worldBounds, Sound soundShoot, HPIndicator hpIndicator) {
        super(atlas.findRegion("main_ship"), 1, 2, 2, soundShoot);
        setHeightProportion(HEIGHT);
        this.bulletPool = bulletPool;
        this.atlas = atlas;
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        this.hpIndicator = hpIndicator;
        this.hpIndicator.setHeight(this.getHeight()*0.25f);
        startNewGame();
    }

    public void startNewGame() {
        pos.x = worldBounds.pos.x;
        this.bulletV.set(0, 0.5f);
        this.bulletHeight = 0.01f;
        this.bulletDamage = 1;
        this.reloadInterval = 0.2f;
        this.hp = MAX_HP;
        flushDestroy();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setBottom(worldBounds.getBottom() + 0.05f);
        hpIndicator.pos.set(pos);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
        hpIndicator.pos.set(pos.x, getBottom() - 0.02f);
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            shoot();
            reloadTimer = 0f;
        }
        stayInside();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (touch.x < worldBounds.pos.x) {
            if (leftPointer != INVALID_POINTER) return false;
            leftPointer = pointer;
            moveLeft();
        } else {
            if (rightPointer != INVALID_POINTER) return false;
            rightPointer = pointer;
            moveRight();
        }
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) {
                moveRight();
            } else {
                stop();
            }
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) {
                moveLeft();
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
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotate(180);
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > pos.y
                || bullet.getTop() < getBottom());
    }

    @Override
    public void destroy() {
        boom();
        hp = 0;
        super.destroy();
    }
}
