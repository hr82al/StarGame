 package ru.geekbrains.hr82al.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.hr82al.base.ActionListener;
import ru.geekbrains.hr82al.base.Base2DScreen;
import ru.geekbrains.hr82al.base.Font;
import ru.geekbrains.hr82al.math.Rect;
import ru.geekbrains.hr82al.pool.EnemyPool;
import ru.geekbrains.hr82al.pool.ExplosionPool;
import ru.geekbrains.hr82al.sprits.Background;
import ru.geekbrains.hr82al.sprits.Bullet;
import ru.geekbrains.hr82al.sprits.ButtonNewGame;
import ru.geekbrains.hr82al.sprits.Enemy;
import ru.geekbrains.hr82al.sprits.HPIndicator;
import ru.geekbrains.hr82al.sprits.MainShip;
import ru.geekbrains.hr82al.sprits.MessageGameOver;
import ru.geekbrains.hr82al.sprits.Star;
import ru.geekbrains.hr82al.pool.BulletPool;
import ru.geekbrains.hr82al.utils.EnemiesEmitter;

public class GameScreen extends Base2DScreen {
    private static final int STARS_NUMBER = 64;
    private enum State {PLAYING, GAME_OVER};
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
    private Font font;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";
    private StringBuilder sbFrags = new StringBuilder();
    private StringBuilder sbHP = new StringBuilder();
    private StringBuilder sbLevel = new StringBuilder();
    private HPIndicator hpIndicator;

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
        hpIndicator = new HPIndicator(MainShip.MAX_HP, MainShip.HEIGHT);
        mainShip = new MainShip(textureAtlas, bulletPool, explosionPool, worldBounds, laserSound, hpIndicator);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, bulletSound);
        enemiesEmitter = new EnemiesEmitter(enemyPool, worldBounds, textureAtlas);
        font = new Font("font/font.fnt", "font/font.png");
        font.setFontSize(0.02f);
        music.setLooping(true);
        music.play();
        startNewGame();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        if (state == State.PLAYING) {
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
            case PLAYING:
                mainShip.update(delta);
                bulletPool.updateActiveObjects(delta);
                enemyPool.updateActiveObjects(delta);
                enemiesEmitter.generate(delta, frags);
                if (mainShip.isDestroyed()) {
                    state = State.GAME_OVER;
                }
                break;
            case GAME_OVER:
                break;
        }
        explosionPool.updateActiveObjects(delta);
        hpIndicator.setHP(mainShip.getHP());
        hpIndicator.update();
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
                mainShip.damage(bullet.getDamage());
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
                    enemy.damage(bullet.getDamage());
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
        printInfo();
        batch.end();
        hpIndicator.draw(shapeRenderer);
    }

    public  void printInfo() {
        sbFrags.setLength(0);
        sbHP.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch,sbFrags.append(FRAGS).append(frags), worldBounds.getLeft(),
                worldBounds.getTop());
        font.draw(batch,sbHP.append(HP).append(mainShip.getHP()), worldBounds.pos.x,
                worldBounds.getTop(), Align.center);
        font.draw(batch,sbLevel.append(LEVEL).append(enemiesEmitter.getLevel()), worldBounds.getRight(),
                worldBounds.getTop(), Align.right);
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
        if (state == State.PLAYING) {
            mainShip.keyDown(keycode);
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyUp(keycode);
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (state == State.PLAYING) {
            mainShip.touchDown(touch, pointer);
        } else {
            buttonNewGame.touchDown(touch, pointer);
        }
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {

        if (state == State.PLAYING) {
            mainShip.touchUp(touch, pointer);
        } else {
            buttonNewGame.touchUp(touch, pointer);
        }
        return super.touchUp(touch, pointer);
    }

    private void startNewGame() {
        state = State.PLAYING;
        enemiesEmitter.setLevel(1);
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
        font.dispose();
        music.dispose();
        super.dispose();
    }
}
