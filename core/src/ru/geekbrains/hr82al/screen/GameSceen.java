 package ru.geekbrains.hr82al.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.geekbrains.hr82al.base.Base2DScreen;
import ru.geekbrains.hr82al.base.Sprite;
import ru.geekbrains.hr82al.math.Rect;
import ru.geekbrains.hr82al.pool.EnemyPool;
import ru.geekbrains.hr82al.pool.ExplosionPool;
import ru.geekbrains.hr82al.sprits.Background;
import ru.geekbrains.hr82al.sprits.Bullet;
import ru.geekbrains.hr82al.sprits.Enemy;
import ru.geekbrains.hr82al.sprits.MainShip;
import ru.geekbrains.hr82al.sprits.Star;
import ru.geekbrains.hr82al.pool.BulletPool;
import ru.geekbrains.hr82al.utils.EnemiesEmitter;

public class GameSceen extends Base2DScreen {
    private static final int STARS_NUMBER = 64;
    private TextureAtlas textureAtlas;
    private Star[] stars;
    private Texture bgTexture;
    private Background background;
    private Sprite gameOver;
    private MainShip mainShip;
    private BulletPool bulletPool;
    private Music music;
    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosionSound;
    private EnemyPool enemyPool;
    private EnemiesEmitter enemiesEmitter;
    private ExplosionPool explosionPool;

    @Override
    public void show() {
        super.show();
        bgTexture = new Texture("bg.png");
        background = new Background(new TextureRegion(bgTexture));
        textureAtlas = new TextureAtlas("mainAtlas.tpack");
        gameOver = new Sprite(textureAtlas.findRegion("message_game_over"));
        gameOver.setHeightProportion(0.07f);
        stars = new Star[STARS_NUMBER];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(textureAtlas);
        }
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        explosionPool = new ExplosionPool(textureAtlas, explosionSound);
        bulletPool = new BulletPool();
        mainShip = new MainShip(textureAtlas, bulletPool, explosionPool, laserSound);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, bulletSound);
        enemiesEmitter = new EnemiesEmitter(enemyPool, worldBounds, textureAtlas);
        music.setLooping(true);
        music.play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        deleteAllDestroyed();
        draw();
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        mainShip.update(delta);
        bulletPool.updateActiveObjects(delta);
        enemyPool.updateActiveObjects(delta);
        explosionPool.updateActiveObjects(delta);
        enemiesEmitter.generate(delta);
    }

    public void checkCollisions() {
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDist = enemy.getHalfWidth() + mainShip.getHalfWidth();
            if (enemy.pos.dst2(mainShip.pos) < minDist * minDist) {
                enemy.destroy();
                mainShip.destroy();
                return;
            }
        }
        List<Bullet> bulletList = bulletPool.getActiveObjects();

        for (Bullet bullet : bulletList) {
            if (bullet.isDestroyed()  || bullet.getOwner() == mainShip) {
                continue;
            }
            if (mainShip.isBulletCollision(bullet)) {
                bullet.destroy();
                mainShip.damage(bullet.getDamge());
                System.out.println("Main ship hp: " + mainShip.getHp());
            }
        }

        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            for (Bullet bullet : bulletList) {
                if (bullet.isDestroyed()  || bullet.getOwner() != mainShip) {
                    continue;
                }
                if (enemy.isBulletCollision(bullet)) {
                    bullet.destroy();
                    enemy.damage(bullet.getDamge());
                }
            }
        }
    }

    public void deleteAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
    }

    public void draw() {
        Gdx.gl.glClearColor(0.3f, 0.88f, 0.6f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        if (!mainShip.isDestroyed()) {
            for (Star star : stars) {
                star.draw(batch);
            }
            bulletPool.drawActiveObjects(batch);
            if (!mainShip.isDestroyed()) {
                mainShip.draw(batch);
            }
            enemyPool.drawActiveObjects(batch);
            explosionPool.drawActiveObjects(batch);
        } else {
            gameOver.draw(batch);
        }
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        mainShip.touchDown(touch, pointer);
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        mainShip.touchUp(touch, pointer);
        return super.touchUp(touch, pointer);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        bgTexture.dispose();
        textureAtlas.dispose();
        bulletSound.dispose();
        laserSound.dispose();
        explosionSound.dispose();
        music.dispose();
        super.dispose();
    }
}
