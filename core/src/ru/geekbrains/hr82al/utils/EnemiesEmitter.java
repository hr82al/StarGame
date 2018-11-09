package ru.geekbrains.hr82al.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.math.Rect;
import ru.geekbrains.hr82al.math.Rnd;
import ru.geekbrains.hr82al.pool.EnemyPool;
import ru.geekbrains.hr82al.sprits.Enemy;

public class EnemiesEmitter {
    private static final float ENEMY_SMALL_HEIGHT = 0.1f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static final float ENEMY_SMALL_BULLET_VY = -0.3f;
    private static final int ENEMY_SMALL_BULLET_DAMAGE = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 3f;
    private static final int ENEMY_SMALL_HP = 1;
    private TextureRegion[] enemySmallRegion;
    private Vector2 enemeySmallV = new Vector2(0f, -0.2f);

    private EnemyPool enemyPool;
    private Rect worldBounds;
    private TextureRegion bulletRegion;
    private float generateInterval = 4f;
    private float generateTimer;

    public EnemiesEmitter(EnemyPool enemyPool, Rect worldBounds, TextureAtlas atlas) {
        this.enemyPool = enemyPool;
        this.worldBounds = worldBounds;
        TextureRegion textureRegion0 = atlas.findRegion("enemy0");
        this.enemySmallRegion = Regions.split(textureRegion0, 1, 2, 2);
        this.bulletRegion = atlas.findRegion("bulletEnemy");
    }

    public void generate(float delta) {
        generateTimer += delta;
        if (generateTimer >= generateInterval) {
            generateTimer = 0f;
            Enemy enemy = enemyPool.obtain();
            enemy.set(
                    enemySmallRegion,
                    enemeySmallV,
                    bulletRegion,
                    ENEMY_SMALL_BULLET_HEIGHT,
                    ENEMY_SMALL_BULLET_VY,
                    ENEMY_SMALL_BULLET_DAMAGE,
                    ENEMY_SMALL_RELOAD_INTERVAL,
                    ENEMY_SMALL_HEIGHT,
                    ENEMY_SMALL_HP
            );
            enemy.setBottom(worldBounds.getTop());
            enemy.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemy.getHalfWidth(),
                    worldBounds.getRight() - enemy.getHalfWidth());
        }

    }
}
