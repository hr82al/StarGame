package ru.geekbrains.hr82al.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.hr82al.math.Rect;

public class Sprite extends Rect {
    protected float angle;
    protected float scale = 1f;  //масштаб
    protected TextureRegion[] regions;
    protected int frame;

    public Sprite(TextureRegion region) {
        if (region == null) {
            throw new NullPointerException("region is null");
        }
        regions = new TextureRegion[1];
        regions[0] = region;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame],         // текущий регион
                getLeft(), getBottom(), //сдвиг текстуры на половину ширины и высоты - влево и вниз
                halfWidth, halfWidth,   // точка вращения
                getWidth(), getHeight(),//ширина и высота
                scale, scale,           //маштаб по x по y
                angle
        );
    }
}
