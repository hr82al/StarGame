package ru.geekbrains.hr82al.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.math.Rect;
import ru.geekbrains.hr82al.pool.BulletPool;
import ru.geekbrains.hr82al.pool.ExplosionPool;
import ru.geekbrains.hr82al.sprits.Bullet;
import ru.geekbrains.hr82al.sprits.Explosion;

public abstract class Ship extends Sprite {
    protected Vector2 v = new Vector2();
    protected BulletPool bulletPool;
    protected ExplosionPool explosionPool;
    protected Rect worldBounds;
    protected Vector2 bulletV = new Vector2();
    protected float bulletHeight;
    protected int bulletDamage;
    protected float reloadInterval;
    protected float reloadTimer;
    protected float damageAnimateInterval = 0.1f;
    protected float damageAnimateTimer;
    protected int hp;
    protected TextureRegion bulletRegion;
    private Sound soundShoot;

    public Ship(TextureRegion region, int rows, int cols, int frames, Sound soundShoot) {
        super(region, rows, cols, frames);
        this.soundShoot = soundShoot;
    }

    public Ship(Sound soundShoot) {
        this.soundShoot = soundShoot;
    }

    protected void stayInside() {
        if(getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        } else if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    protected void stop() {
        v.setZero();
    }

    protected void shoot() {
        Bullet bullet = bulletPool.obtain();
        soundShoot.play();
        bullet.set(this, bulletRegion, pos,
                bulletV, bulletHeight, worldBounds, bulletDamage);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        damageAnimateInterval += delta;
        if (damageAnimateInterval >= damageAnimateInterval) {
            frame = 0;
        }
    }

    public void boom() {
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(), pos);
    }


    public void damage(int damage) {
        frame = 1;
        damageAnimateTimer = 0;
        hp -= damage;
        if (hp <= 0) {
            destroy();
        }
    }

    public int getHp() {
        return hp;
    }
}
