package ru.geekbrains.hr82al.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.base.Base2DScreen;
import ru.geekbrains.hr82al.math.Rect;
import ru.geekbrains.hr82al.pool.EnemyPool;
import ru.geekbrains.hr82al.sprits.Background;
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
    private MainShip mainShip;
    private BulletPool bulletPool;
    private Music music;
    private Sound laserSound;
    private Sound bulletSound;
    private EnemyPool enemyPool;
    private EnemiesEmitter enemiesEmitter;

    @Override
    public void show() {
        super.show();
        bgTexture = new Texture("bg.png");
        background = new Background(new TextureRegion(bgTexture));
        textureAtlas = new TextureAtlas("mainAtlas.tpack");
        stars = new Star[STARS_NUMBER];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(textureAtlas);
        }
        bulletPool = new BulletPool();
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        mainShip = new MainShip(textureAtlas, bulletPool, laserSound);
        enemyPool = new EnemyPool(bulletPool, worldBounds, bulletSound);
        enemiesEmitter = new EnemiesEmitter(enemyPool, worldBounds, textureAtlas);
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
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
        enemiesEmitter.generate(delta);
    }

    public void checkCollisions() {

    }

    public void deleteAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
    }

    public void draw() {
        Gdx.gl.glClearColor(0.3f, 0.88f, 0.6f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        mainShip.draw(batch);
        bulletPool.drawActiveObjects(batch);
        enemyPool.drawActiveObjects(batch);
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
        music.dispose();
        super.dispose();
    }
}
