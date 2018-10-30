package ru.geekbrains.hr82al.sprits;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.hr82al.base.Sprite;
import ru.geekbrains.hr82al.math.Rect;
import ru.geekbrains.hr82al.math.Rnd;

public class Star extends Sprite {
    private Vector2 velosity = new Vector2();
    private Rect worldBounds;

    public Star(TextureAtlas atlas) {
        super(atlas.findRegion("star"));
        setHeightProportion(0.01f);
        this.velosity.set(Rnd.nextFloat(-0.005f, 0.005f), Rnd.nextFloat(-0.1f, -0.01f));
    }

    @Override
    public void resize(Rect worldBounds) {//срабатывает при запуске игры
        this.worldBounds = worldBounds;
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(posX, posY);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(velosity, delta);
        checkAndHandleBounds();
    }

    private void checkAndHandleBounds() {
        if (getRight() < worldBounds.getLeft()) setLeft(worldBounds.getRight());
        if (getLeft() > worldBounds.getRight()) setRight(worldBounds.getLeft());
        if (getTop() < worldBounds.getBottom()) setBottom(worldBounds.getTop());
        if (getBottom() > worldBounds.getTop()) setTop(worldBounds.getBottom());
    }
}
