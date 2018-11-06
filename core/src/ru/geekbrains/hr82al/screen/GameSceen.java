package ru.geekbrains.hr82al.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import ru.geekbrains.hr82al.base.Base2DScreen;
import ru.geekbrains.hr82al.sprits.Star;

public class GameSceen extends Base2DScreen {
    @Override
    public void show() {
        super.show();
    }

    public void draw() {
        Gdx.gl.glClearColor(0.3f, 0.88f, 0.6f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        batch.end();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
