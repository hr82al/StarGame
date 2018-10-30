package ru.geekbrains.hr82al.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.base.Base2DScreen;
import ru.geekbrains.hr82al.math.Rect;
import ru.geekbrains.hr82al.sprits.Background;
import ru.geekbrains.hr82al.sprits.Star;
import ru.geekbrains.hr82al.sprits.ScaledButton;

public class MenuScreen extends Base2DScreen {
    private Texture bgTexture;
    private TextureAtlas textureAtlas;
    private Background background;
    private Star[] stars;
    private ScaledButton exitButton;
    private ScaledButton playButton;
    private static final int STARS_NUMBER = 256;

    @Override
    public void show() {
        super.show();
        bgTexture = new Texture("bg.png");
        background = new Background(new TextureRegion(bgTexture));
        textureAtlas = new TextureAtlas("menuAtlas.tpack");
        stars = new Star[STARS_NUMBER];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(textureAtlas);
        }
        exitButton = new ScaledButton(textureAtlas, "btExit", 0);
        playButton = new ScaledButton(textureAtlas, "btPlay", 1);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
    }

    public void draw() {
        Gdx.gl.glClearColor(0.3f, 0.88f, 0.6f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        exitButton.draw(batch);
        playButton.draw(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        exitButton.resize();
        playButton.resize();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        exitButton.touchDown(touch, pointer);
        playButton.touchDown(touch, pointer);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        exitButton.touchUp(touch, pointer);
        playButton.touchUp(touch, pointer);
        return false;
    }

    @Override
    public void dispose() {
        bgTexture.dispose();
        textureAtlas.dispose();
        super.dispose();
    }

    @Override
    public void hide() {
        dispose();
    }
}
