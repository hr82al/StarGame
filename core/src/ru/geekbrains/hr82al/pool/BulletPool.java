package ru.geekbrains.hr82al.pool;

import ru.geekbrains.hr82al.base.SpritesPool;
import ru.geekbrains.hr82al.sprits.Bullet;

public class BulletPool extends SpritesPool<Bullet> {
    @Override
    protected Bullet newObject() {
        return new Bullet();
    }
}
