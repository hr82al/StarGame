package ru.geekbrains.hr82al.sprits;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.base.Ship;
import ru.geekbrains.hr82al.math.Rect;
import ru.geekbrains.hr82al.pool.BulletPool;
import ru.geekbrains.hr82al.pool.ExplosionPool;

public class Enemy extends Ship {
    private enum State {DESCENT, FIGHT};
    private State state;
    private Vector2 v0 = new Vector2();
    private Vector2 descenV = new Vector2(0f, -0.2f); // The initial velocity.

    public Enemy(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, Sound soundShot) {
        super(soundShot);
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        this.v.set(v0); //The initial velocity of the ship.
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
        switch (state) {
            case DESCENT:
                if (getTop() <= worldBounds.getTop())  {
                    v.set(v0);
                    state = State.FIGHT;
                }
                break;
            case FIGHT:
                reloadTimer += delta;
                if (reloadTimer >= reloadInterval) {
                    shoot();
                    reloadTimer = 0f;
                }
                if (getBottom() < worldBounds.getBottom()) {
                    boom();
                    destroy();
                }
                break;
        }
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
        this.bulletRegion = bulletRegion;
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0f, bulletVY);
        this.bulletDamage = bulletDamage;
        this.reloadInterval = reloadInterval;
        this.reloadTimer = reloadInterval;
        this.hp = hp;
        setHeightProportion(height);
        this.v0.set(v0);
        v.set(descenV);
        state = State.DESCENT;
    }
}
