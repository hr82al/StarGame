package ru.geekbrains.hr82al.pool;

import com.badlogic.gdx.audio.Sound;

import ru.geekbrains.hr82al.base.SpritesPool;
import ru.geekbrains.hr82al.math.Rect;
import ru.geekbrains.hr82al.sprits.Enemy;
import ru.geekbrains.hr82al.sprits.Explosion;

public class EnemyPool extends SpritesPool<Enemy> {
    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private Rect worldBounds;
    private Sound soundShoot;

    public EnemyPool(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, Sound soundShoot) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        this.soundShoot = soundShoot;
    }

    @Override
    protected Enemy newObject() {
        return new Enemy(bulletPool, explosionPool, worldBounds, soundShoot);
    }
}
