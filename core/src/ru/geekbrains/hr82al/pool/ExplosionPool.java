package ru.geekbrains.hr82al.pool;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.hr82al.base.SpritesPool;
import ru.geekbrains.hr82al.sprits.Explosion;

public class ExplosionPool extends SpritesPool<Explosion> {
    private TextureRegion region;
    private Sound sound;

    public ExplosionPool(TextureAtlas atlas, Sound sound) {
        this.region = atlas.findRegion("explosion");
        this.sound = sound;
    }

    @Override
    protected Explosion newObject() {
        return new Explosion(region, 9, 9, 74, sound);
    }
}
