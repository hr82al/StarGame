package ru.geekbrains.hr82al;

import com.badlogic.gdx.Game;

import ru.geekbrains.hr82al.screen.MenuScreen;

public class Star2DGame extends Game {
    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }
}
