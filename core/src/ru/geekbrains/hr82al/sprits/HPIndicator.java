package ru.geekbrains.hr82al.sprits;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ru.geekbrains.hr82al.base.Sprite;

public class HPIndicator extends Sprite {
    private float degrees;
    private float start;
    private float radius;
    private int hp;
    private int maxHp;
    Color color = new Color(Color.GREEN);

    public HPIndicator(int maxHp, float height) {
        this.maxHp = maxHp;
        setHeight(height);
    }

    public void update() {
        setRate((float)hp / (float)maxHp);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.arc(pos.x, pos.y, radius, start, degrees, 64);
        shapeRenderer.end();
    }

    private void setRate(float rate) {
        degrees = rate  * 360f;
        start = (360f - degrees) / 2f + 270f ;
        color.set(1f - rate, rate, 0,0);
    }

    public void setHeight(float height) {
        radius = height / 2f;
    }

    public void setHP(int hp) {
        this.hp = hp;
    }
}
