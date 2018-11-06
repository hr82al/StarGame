package ru.geekbrains.hr82al.sprits;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.hr82al.base.Sprite;

public class MainShip extends Sprite {
    public MainShip(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"));
        setHeightProportion(0.15f);
    }

}
