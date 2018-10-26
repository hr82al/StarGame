package ru.geekbrains.hr82al.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.base.Base2DScreen;

public class MenuScreen extends Base2DScreen {
    private Texture img;
    private Texture movedImg;

    private Vector2 touch;
    private Vector2 position;
    private Vector2 direction;
    private int counter = 0;

    private static final float VELOCITY = 0.14f;

    @Override
    public void show() {
        super.show();
        direction = new Vector2();
        img = new Texture("background.jpg");
        movedImg = new Texture("badlogic.jpg");
        touch = new Vector2();
        position = new Vector2(0f, 0f);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, -21f, -21f, 42f, 42f);
        //batch.draw(movedImg, 0,0, 21f, 21f);
        batch.draw(movedImg, position.x, position.y, 12f, 12f);
        batch.end();
        stepMove();
    }

    private void stepMove() {
        if (counter-- > 0) {
            position.add(direction);
        } else {
            position.set(touch);
        }
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        this.touch.set(touch);
        setDirection();
        return false;
    }

    private void setDirection() {
        direction.set(touch);
        counter =  (int)(direction.sub(position).len() / VELOCITY);
        direction.nor().scl(VELOCITY);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        img.dispose();
        super.dispose();
    }
}
