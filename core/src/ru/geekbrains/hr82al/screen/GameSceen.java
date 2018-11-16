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

import jdk.nashorn.internal.runtime.WithObject;
import ru.geekbrains.hr82al.base.ActionListener;
import ru.geekbrains.hr82al.base.Base2DScreen;
import ru.geekbrains.hr82al.base.Sprite;
import ru.geekbrains.hr82al.math.Rect;
import ru.geekbrains.hr82al.pool.EnemyPool;
import ru.geekbrains.hr82al.pool.ExplosionPool;
import ru.geekbrains.hr82al.sprits.Background;
import ru.geekbrains.hr82al.sprits.Bullet;
import ru.geekbrains.hr82al.sprits.ButtonNewGame;
import ru.geekbrains.hr82al.sprits.Enemy;
import ru.geekbrains.hr82al.sprits.MainShip;
import ru.geekbrains.hr82al.sprits.MessageGameOver;
import ru.geekbrains.hr82al.sprits.Star;
import ru.geekbrains.hr82al.pool.BulletPool;
import ru.geekbrains.hr82al.utils.EnemiesEmitter;

public class GameSceen extends Base2DScreen {
    private static final int STARS_NUMBER = 64;
    private enum State {PLAING, GAME_OVER};
    private State state;
    private int frags;
    private TextureAtlas textureAtlas;
    private Star[] stars;
    private Texture bgTexture;
    private Background background;
    private MessageGameOver messageGameOver;
    private ButtonNewGame buttonNewGame;
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
        messageGameOver = new MessageGameOver(textureAtlas);
        buttonNewGame = new ButtonNewGame(textureAtlas);
        buttonNewGame.setActionListener(new ActionListener() {
            @Override
            public void actionPerformed(Object src) {
                startNewGame();
            }
        });
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
        mainShip = new MainShip(textureAtlas, bulletPool, explosionPool, worldBounds, laserSound);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, bulletSound);
        enemiesEmitter = new EnemiesEmitter(enemyPool, worldBounds, textureAtlas);
        music.setLooping(true);
        music.play();
        startNewGame();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        if (state == State.PLAING) {
            checkCollisions();
        }
        deleteAllDestroyed();
        draw();
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        switch (state) {
            case PLAING:
                mainShip.update(delta);
                bulletPool.updateActiveObjects(delta);
                enemyPool.updateActiveObjects(delta);
                enemiesEmitter.generate(delta);
                if (mainShip.isDestroyed()) {
                    state = State.GAME_OVER;
                }
                break;
            case GAME_OVER:
                break;
        }
        explosionPool.updateActiveObjects(delta);
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
                state = State.GAME_OVER;
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
                if (mainShip.isDestroyed()) {
                    state = State.GAME_OVER;
                }
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
                    if (enemy.isDestroyed()) {
                        frags++;
                    }
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
        for (Star star : stars) {
            star.draw(batch);
        }
        if (state == State.GAME_OVER) {
            messageGameOver.draw(batch);
            buttonNewGame.draw(batch);
        } else {
            bulletPool.drawActiveObjects(batch);
            mainShip.draw(batch);
            enemyPool.drawActiveObjects(batch);
        }
        explosionPool.drawActiveObjects(batch);
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
        buttonNewGame.resize(worldBounds);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAING) {
            mainShip.keyDown(keycode);
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAING) {
            mainShip.keyUp(keycode);
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (state == State.PLAING) {
            mainShip.touchDown(touch, pointer);
        } else {
            buttonNewGame.touchDown(touch, pointer);
        }
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {

        if (state == State.PLAING) {
            mainShip.touchUp(touch, pointer);
        } else {
            buttonNewGame.touchUp(touch, pointer);
        }
        return super.touchUp(touch, pointer);
    }

    private void startNewGame() {
        state = State.PLAING;
        frags = 0;
        mainShip.startNewGame();
        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
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
