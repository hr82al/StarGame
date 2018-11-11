package ru.geekbrains.hr82al.pool;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.hr82al.base.SpritesPool;
import ru.geekbrains.hr82al.sprits.Explosion;

public class ExplosionPool extends SpritesPool<Explosion> {
    private TextureRegion region;

    public ExplosionPool(TextureAtlas atlas) {
        this.region = atlas.findRegion("explosion");
    }

    @Override
    protected Explosion newObject() {
        return new Explosion(region, 9, 9, 74);
    }
}
