package ru.geekbrains.hr82al.pool;

import com.badlogic.gdx.audio.Sound;

import ru.geekbrains.hr82al.base.SpritesPool;
import ru.geekbrains.hr82al.math.Rect;
import ru.geekbrains.hr82al.sprits.Enemy;

public class EnemyPool extends SpritesPool<Enemy> {
    private BulletPool bulletPool;
    private Rect worldBounds;
    private Sound soundShoot;

    public EnemyPool(BulletPool bulletPool, Rect worldBounds, Sound soundShoot) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        this.soundShoot = soundShoot;
    }

    @Override
    protected Enemy newObject() {
        return new Enemy(bulletPool, worldBounds, soundShoot);
    }
}
