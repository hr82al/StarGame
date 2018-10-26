package ru.geekbrains.hr82al.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.math.MatrixUtils;
import ru.geekbrains.hr82al.math.Rect;

public class Base2DScreen implements Screen, InputProcessor {
    protected SpriteBatch batch;

    private Rect screeBounds; // границы облачти рисования в пикселях
    private Rect worldBounds; // границы проекции мпровых координат
    private Rect glBounds; // гранины проекции world - gl

    protected Matrix4 worldToGl;
    protected Matrix3 screenToWold;

    private Vector2 touch;

    private final static float WOLD_BOUND = 42f;

    @Override
    public void show() {
        System.out.println("show");
        this.batch = new SpriteBatch();
        Gdx.input.setInputProcessor(this);
        this.screeBounds = new Rect();
        this.worldBounds = new Rect();
        this.glBounds = new Rect(0, 0, 1f, 1f);
        this.worldToGl = new Matrix4();
        this.screenToWold = new Matrix3();
        this.touch = new Vector2();
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resize width: " + width + " height: " + height);
        screeBounds.setSize(width, height);
        screeBounds.setLeft(0);
        screeBounds.setBottom(0);

        float aspect = width /(float) height;
        worldBounds.setHeight(WOLD_BOUND);
        worldBounds.setWidth(WOLD_BOUND*aspect);
        MatrixUtils.calcTransitionMatrix(worldToGl, worldBounds, glBounds);
        batch.setProjectionMatrix(worldToGl);
        MatrixUtils.calcTransitionMatrix(screenToWold, screeBounds, worldBounds);
    }

    @Override
    public void pause() {
        System.out.println("pause");
    }

    @Override
    public void resume() {
        System.out.println("resume");
    }

    @Override
    public void hide() {
        System.out.println("hide");
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        System.out.println("dispose");
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, screeBounds.getHeight() - screenY).mul(screenToWold);
        touchDown(touch, pointer);
        return false;
    }

    public boolean touchDown(Vector2 touch, int pointer) {
        System.out.println("touchDown touch.x = " + touch.x + " touch.y = " + touch.y);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, screeBounds.getHeight() - screenY).mul(screenToWold);
        touchUp(touch, pointer);
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer) {
        System.out.println("touchUp touch.x = " + touch.x + " touch.y = " + touch.y);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
