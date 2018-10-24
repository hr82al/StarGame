package ru.geekbrains.hr82al.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.base.Base2DScreen;

public class MenuScreen extends Base2DScreen {
    private SpriteBatch batch;
    private Texture img;
    private Texture movedImg;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 direction;
    private Vector2 destination;
    private int steps;

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        img = new Texture("background.jpg");
        movedImg = new Texture("badlogic.jpg");
        position = new Vector2(0f, 0f);
        velocity = new Vector2(0.7f, 0.9f);
        direction = velocity.cpy();
        destination = new Vector2(0f, 0f);
        setDirection(position, destination);
    }

    private void setDirection(Vector2 position, Vector2 destination) {
        Vector2 tmp = destination.cpy();
        tmp.sub(position);
        steps = (int)tmp.len() / (int)velocity.len();
        tmp.nor();
        direction = velocity.cpy();
        direction.scl(tmp);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(movedImg, position.x, position.y);
        batch.end();
        moveToDestination();
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        destination.set(screenX, Gdx.graphics.getHeight() - screenY);
        setDirection(position, destination);
        return false;
    }

    private void moveToDestination() {
        if (steps-- > 0) {
            position.add(direction);
        } else if (position.x != destination.x && position.y != destination.y){
            setDirection(position, destination);
        }
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        super.dispose();
    }
}
