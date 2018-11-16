package ru.geekbrains.hr82al.sprits;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ButtonNewGame extends ScaledButton {
    public ButtonNewGame(TextureAtlas atlas) {
        super(atlas.findRegion("button_new_game"));
        setHeightProportion(0.05f);
        setTop(-0.12f);
    }
}
