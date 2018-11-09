package ru.geekbrains.hr82al.sprits;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.base.Ship;
import ru.geekbrains.hr82al.math.Rect;
import ru.geekbrains.hr82al.pool.BulletPool;

public class Enemy extends Ship {
    private Vector2 v0 = new Vector2();

    public Enemy(BulletPool bulletPool, Rect worldBounds, Sound soundShot) {
        super(soundShot);
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        this.v.set(v0); //The initial velocity of the ship.
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int bulletDamage,
            float reloadInterval,
            float height,
            int hp
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0f, bulletVY);
        this.bulletDamage = bulletDamage;
        this.reloadInterval = reloadInterval;
        this.hp = hp;
        setHeightProportion(height);
        v.set(v0);
    }
}
