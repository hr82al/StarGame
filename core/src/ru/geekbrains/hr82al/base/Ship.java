package ru.geekbrains.hr82al.base;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.math.Rect;
import ru.geekbrains.hr82al.pool.BulletPool;
import ru.geekbrains.hr82al.sprits.Bullet;

public class Ship extends Sprite {
    protected Vector2 v = new Vector2();
    protected BulletPool bulletPool;
    protected Rect worldBounds;
    protected TextureAtlas atlas;
    protected Vector2 bulletV = new Vector2();
    protected float bulletHeight;
    protected int bulletDamage;
    protected float reloadInterval;
    protected float reloadTimer;

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    protected void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, atlas.findRegion("bulletMainShip"), pos,
                bulletV, bulletHeight, worldBounds, bulletDamage);
    }
}
